package com.chat;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ChatClient  {
    private final String name;

    public ChatClient(String name) {
        this.name = name;
    }

    public void init(ManagedChannel channel) throws IOException {
        ChatServiceGrpc.ChatServiceStub asyncChatService = ChatServiceGrpc.newStub(channel);

        // Listens for messages coming from the client
        StreamObserver<ChatMessage> toServer = asyncChatService.chat(createObserver());

        InputStream is = System.in;
        BufferedReader br =  new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.equalsIgnoreCase("")) {
                break;
            }
        }
        toServer.onNext(ChatMessage.newBuilder()
                .setFrom(name)
                .setMessage(line)
                .build());
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

    public static void main(String[] args) throws IOException {
        ChatClient chatClient = new ChatClient(System.getProperty("username"));
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost", 8080).usePlaintext().build();
        chatClient.init(channel);
    }
}
