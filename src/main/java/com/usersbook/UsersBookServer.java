package com.usersbook;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

public class UsersBookServer {
    public static void main( String[] args ) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(8080)
                .addService(new UsersBookServiceImpl())
                .build();

        server.start();
        System.out.println("Server Started");
        server.awaitTermination();
    }
}
