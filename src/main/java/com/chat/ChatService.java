package com.chat;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.LinkedHashSet;

public class ChatService extends ChatServiceGrpc.ChatServiceImplBase{
    private static final LinkedHashSet<StreamObserver<ChatMessageFromServer>> observers = new LinkedHashSet<>();

    @Override
    public StreamObserver<ChatMessage> chat(StreamObserver<ChatMessageFromServer> responseObserver) {
        observers.add(responseObserver);

        return new StreamObserver<ChatMessage>() {
            //Client sends request
            @Override
            public void onNext(ChatMessage chatMessageFromClient) {
                System.out.printf("%s sent a message\n", chatMessageFromClient.getFrom());
                ChatMessageFromServer msg = ChatMessageFromServer.newBuilder()
                        .setMessage(chatMessageFromClient)
                        .build();
                // Broadcast message to every connected client
                observers.stream()
                        .forEach(existingConnection -> existingConnection.onNext(msg));
            }

            @Override
            public void onError(Throwable throwable) {
                observers.remove(responseObserver);
            }

            @Override
            public void onCompleted() {
                observers.remove(responseObserver);
            }
        };
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(8080)
                .addService(new ChatService())
                .build();

        server.start();
        System.out.println("Server started");
        server.awaitTermination();
    }
}
