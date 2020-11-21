package com.addressbook;

import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

/**
 * Hello world!
 *
 */
public class AddressBookServer {
    public static void main( String[] args ) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(8080)
                .addService(new AddressBookServiceImpl())
                .build();

        server.start();
        server.awaitTermination();
    }
}
