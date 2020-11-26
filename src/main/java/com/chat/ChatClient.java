package com.chat;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ChatClient  {
    private final String name;

    public ChatClient(String name) {
        this.name = name;
    }

    public void init(ManagedChannel channel) {
        final CountDownLatch finishLatch = new CountDownLatch(1);
        ChatServiceGrpc.ChatServiceStub asyncChatService = ChatServiceGrpc.newStub(channel);

        // Listens for messages coming from the client
        StreamObserver<ChatMessage> toServer = asyncChatService.chat(createObserver());

        Scanner in = new Scanner(System.in);
        System.out.println("First message");
        String msg = in.nextLine();
        do {
                toServer.onNext(ChatMessage.newBuilder()
                        .setFrom(name)
                        .setMessage(msg)
                        .build());
                msg = in.nextLine();
        } while(!msg.equalsIgnoreCase("logout"));

        try {
            System.out.println("Logging out");
            finishLatch.await(5, TimeUnit.SECONDS);
            toServer.onCompleted();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private StreamObserver<ChatMessageFromServer> createObserver() {
        return new StreamObserver<ChatMessageFromServer>() {
            @Override
            public void onNext(ChatMessageFromServer chatMessageFromServer) {
                String message = String.format("%s: %s", chatMessageFromServer.getMessage().getFrom(),
                        chatMessageFromServer.getMessage().getMessage());
                System.out.println(message);
            }

            @Override
            public void onError(Throwable throwable) {
                throwable.printStackTrace();
            }

            @Override
            public void onCompleted() {
                // do nothing
            }
        };
    }

    public static void main(String[] args)  {
        ChatClient chatClient = new ChatClient(System.getProperty("username"));
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080).usePlaintext().build();
        System.out.println("Chat started");
        chatClient.init(channel);
    }
}
