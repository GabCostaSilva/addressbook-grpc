package com.usersbook;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.*;
import java.io.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class UsersBookClient {
    public static void main(String[] args) throws IOException  {
        Scanner scan = new Scanner(System.in);
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
            .usePlaintext() // no need for tls
            .build();

        Boolean rodando = true;
        while(rodando){
            System.out.println("Selecione uma opcao: ");
            System.out.println("1 - Add User");
            System.out.println("2 - Lista User");
            System.out.println("3 - Add Lista User");
            System.out.println("exit - Sair\n");
            System.out.print("Opcao:");
            switch (scan.next()) {
                /** Unário **/
                // case "get":
                //     getUserClient(channel);
                //     break;
    
                /** Unário **/
                case "1":
                    promptForAddUser(channel, scan, System.out);
                    break;
            
                /** A server-side streaming **/
                case "2":
                    getListUserClient(channel);
                    break;
    
                /** A client-side streaming **/
                case "3":
                    addListUserClient(channel);
                    break;
    
                case "exit":
                    rodando = false;
                default:
                    System.out.println("Escolha uma opcao.");
            }
        }

        /** Bidirecional **/
        // addMultipleContactClient(channel);

        System.out.println("Shutting down channel");
        channel.shutdown();
    }

    /** Essa função irá usar o prompt para poder cadastrar um novo usuário **/
    static void promptForAddUser(ManagedChannel channel, Scanner stdin, PrintStream stdout) throws IOException {
        User.Builder user = User.newBuilder();

        stdout.print("Enter name: ");
        user.setName(stdin.next());

        stdout.print("Enter cpf: ");
        user.setCpf(stdin.next());

        stdout.print("Enter idade: ");
        user.setIdade(stdin.nextInt());

        stdout.print("Enter telefone: ");
        user.setTelefone(stdin.next());

        stdout.print("Enter email address (blank for none): ");
        String email = stdin.next();
        if(!email.isEmpty())
            user.setEmail(email);

        addUserClient(channel, user);
    }
    
    /**  **/
    private static void getUserClient(ManagedChannel channel) {
        UsersBookServiceGrpc.UsersBookServiceBlockingStub stub = UsersBookServiceGrpc.newBlockingStub(channel);

        User user = User.newBuilder()
            .setName("John")
            .setCpf("111111")
            .setIdade(30)
            .setTelefone("32999999")
            .setEmail("grupo@gmail.com")
            .build();

        /**  Monta a request **/
        UserRequest request = UserRequest.newBuilder()
            .setUser(user)
            .build();

        /** Irá printar todas as respostas recebidas do observer **/
        UserResponse response = stub.getUser(request);
        System.out.println(response.getResult());
    }

    /** Adiciona um usuário **/
    private static void addUserClient(ManagedChannel channel, User.Builder user) throws IOException  {
        UsersBookServiceGrpc.UsersBookServiceBlockingStub stub = UsersBookServiceGrpc.newBlockingStub(channel);

        /**  Monta a request **/
        UserRequest request = UserRequest.newBuilder()
            .setUser(user)
            .build();

        /** Irá printar todas as respostas recebidas do observer **/
        UserResponse response = stub.getUser(request);

        try {
            user.mergeFrom(new FileInputStream("Livro de Usuarios"));
        } catch (FileNotFoundException e) {
            System.out.println("Livro de Usuarios" + ": File not found.  Creating a new file.");
        }

        OutputStream output = new BufferedOutputStream(new FileOutputStream("Livro de Usuarios"));
        user.build().writeTo(output);
        output.close();

        System.out.println(response.getResult());
    }

    private static void getListUserClient(ManagedChannel channel) {
        UsersBookServiceGrpc.UsersBookServiceBlockingStub stub = UsersBookServiceGrpc.newBlockingStub(channel);

        User user = User.newBuilder()
            .setName("John")
            .setCpf("111111")
            .setIdade(30)
            .setTelefone("32999999")
            .setEmail("grupo@gmail.com")
            .build();

        /**  Monta a request **/
        UserRequest request = UserRequest.newBuilder()
            .setUser(user)
            .build();

        /** Irá printar todas as respostas recebidas do observer **/
        stub.getListUser(request).forEachRemaining(response -> {
            System.out.println(response.getResult());
        });
    }

    private static void addListUserClient(ManagedChannel channel) {
        // create an asynchronous client
        UsersBookServiceGrpc.UsersBookServiceStub asyncClient = UsersBookServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<UserRequest> requestObserver = asyncClient.addListUser(new StreamObserver<UserResponse>() {
            @Override
            public void onNext(UserResponse value) {
                // we get a response from the server
                System.out.println("Received a response from the server");
                System.out.println(value.getResult());
                // onNext will be called only once
            }

            @Override
            public void onError(Throwable t) {
                // we get an error from the server
            }

            @Override
            public void onCompleted() {
                // the server is done sending us data
                // onCompleted will be called right after onNext()
                System.out.println("Server has completed sending us something");
                latch.countDown();
            }
        });

        User user = User.newBuilder()
            .setName("John")
            .setCpf("111111")
            .setIdade(30)
            .setTelefone("32999999")
            .setEmail("grupo@gmail.com")
            .build();

        // streaming message #1
        System.out.println("sending message 1");
        requestObserver.onNext(UserRequest.newBuilder()
            .setUser(user)
            .build()
        );

        // streaming message #2
        System.out.println("sending message 2");
        requestObserver.onNext(UserRequest.newBuilder()
            .setUser(user)
            .build()
        );

        // streaming message #3
        System.out.println("sending message 3");
        requestObserver.onNext(UserRequest.newBuilder()
            .setUser(user)
            .build()
        );

        // we tell the server that the client is done sending data
        requestObserver.onCompleted();

        try {
            latch.await(3L, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // private static void addMultipleContactClient(ManagedChannel channel) {
    //     UsersBookServiceGrpc.UsersBookServiceStub asyncClient = UsersBookServiceGrpc.newStub(channel);


    //     CountDownLatch latch = new CountDownLatch(1);

    //     StreamObserver<UserRequest> requestObserver = asyncClient.addMultipleContact(new StreamObserver<UserResponse>() {
    //         @Override
    //         public void onNext(UserResponse value) {
    //             System.out.println("Response from server: " + value.getResult());
    //         }

    //         @Override
    //         public void onError(Throwable t) {
    //             latch.countDown();
    //         }

    //         @Override
    //         public void onCompleted() {
    //             System.out.println("Servidor Pronto!");
    //             latch.countDown();
    //         }
    //     });

    //     Arrays.asList("Fernando Kendy", "Victor Kóji", "Gabriel Costa").forEach(
    //             name -> {
    //                 System.out.println("Enviando: " + name);
    //                 requestObserver.onNext(UserRequest.newBuilder()
    //                         .setName(name)
    //                         .build());
    //                 try {
    //                     Thread.sleep(100);
    //                 } catch (InterruptedException e) {
    //                     e.printStackTrace();
    //                 }
    //             }
    //     );

    //     requestObserver.onCompleted();

    //     try {
    //         latch.await(3, TimeUnit.SECONDS);
    //     } catch (InterruptedException e) {
    //         e.printStackTrace();
    //     }
    // }
}
