package com.addressbook;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.Arrays;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class AddressBookClient {
    public static void main(String[] args) {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080)
                .usePlaintext() // no need for tls
                .build();
        // Full Async
        // AddressBookServiceGrpc.newStub();
        // Future API
        // AddressBookServiceGrpc.newFutureStub();
        // Block execution until result is done
        // AddressBookServiceGrpc.AddressBookServiceBlockingStub stub =  AddressBookServiceGrpc.newBlockingStub(channel);

        /** Unário **/
        // getContactClient(channel);

        /** A server-side streaming **/
        // getListContactClient(channel);

        // addListContactClient(channel);
        addMultipleContactClient(channel);

        System.out.println("Shutting down channel");
        channel.shutdown();
    }

     
    private static void getContactClient(ManagedChannel channel) {
        AddressBookServiceGrpc.AddressBookServiceBlockingStub stub = AddressBookServiceGrpc.newBlockingStub(channel);

        /**  Monta a request **/
        ContactRequest request = ContactRequest.newBuilder()
            .setName("John")
            .setNumber(111111)
            .setStreet("Alamo St.")
            .addPhoneList("32999999")
            .setPhoneType(PhoneType.WORK)
            .putCoordinates("37.3861", "122.0839")
            .build();

        /** Irá printar todas as respostas recebidas do observer **/
        ContactResponse response = stub.getContact(request);
        System.out.println(response.getResult());
    }

    private static void getListContactClient(ManagedChannel channel) {
        AddressBookServiceGrpc.AddressBookServiceBlockingStub stub = AddressBookServiceGrpc.newBlockingStub(channel);

        /**  Monta a request **/
        ContactRequest request = ContactRequest.newBuilder()
            .setName("John")
            .setNumber(111111)
            .setStreet("Alamo St.")
            .addPhoneList("32999999")
            .setPhoneType(PhoneType.WORK)
            .putCoordinates("37.3861", "122.0839")
            .build();

        /** Irá printar todas as respostas recebidas do observer **/
        stub.getListContact(request).forEachRemaining(response -> {
            System.out.println(response.getResult());
        });
    }

    private static void addListContactClient(ManagedChannel channel) {
        // create an asynchronous client
        AddressBookServiceGrpc.AddressBookServiceStub asyncClient = AddressBookServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<ContactRequest> requestObserver = asyncClient.addListContact(new StreamObserver<ContactResponse>() {
            @Override
            public void onNext(ContactResponse value) {
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

        // streaming message #1
        System.out.println("sending message 1");
        requestObserver.onNext(ContactRequest.newBuilder()
            .setName("Kendy123")
            .setNumber(123213)
            .setStreet("Alamo St.")
            .addPhoneList("32999999")
            .setPhoneType(PhoneType.WORK)
            .putCoordinates("37.3861", "122.0839")
            .build()
        );

        // streaming message #2
        System.out.println("sending message 2");
        requestObserver.onNext(ContactRequest.newBuilder()
            .setName("Gabriel")
            .setNumber(123213)
            .setStreet("Alamo St.")
            .addPhoneList("32999999")
            .setPhoneType(PhoneType.WORK)
            .putCoordinates("37.3861", "122.0839")
            .build()
        );

        // streaming message #3
        System.out.println("sending message 3");
        requestObserver.onNext(ContactRequest.newBuilder()
            .setName("Koji")
            .setNumber(123)
            .setStreet("Alamo St.")
            .addPhoneList("32999999")
            .setPhoneType(PhoneType.WORK)
            .putCoordinates("37.3861", "122.0839")
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

    private static void addMultipleContactClient(ManagedChannel channel) {
        AddressBookServiceGrpc.AddressBookServiceStub asyncClient = AddressBookServiceGrpc.newStub(channel);


        CountDownLatch latch = new CountDownLatch(1);

        StreamObserver<ContactRequest> requestObserver = asyncClient.addMultipleContact(new StreamObserver<ContactResponse>() {
            @Override
            public void onNext(ContactResponse value) {
                System.out.println("Response from server: " + value.getResult());
            }

            @Override
            public void onError(Throwable t) {
                latch.countDown();
            }

            @Override
            public void onCompleted() {
                System.out.println("Servidor Pronto!");
                latch.countDown();
            }
        });

        Arrays.asList("Fernando Kendy", "Victor Kóji", "Gabriel Costa").forEach(
                name -> {
                    System.out.println("Enviando: " + name);
                    requestObserver.onNext(ContactRequest.newBuilder()
                            .setName(name)
                            .build());
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        );

        requestObserver.onCompleted();

        try {
            latch.await(3, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
