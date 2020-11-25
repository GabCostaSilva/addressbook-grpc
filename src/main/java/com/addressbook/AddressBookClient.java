package com.addressbook;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

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

        /* Unário **/
        getContactClient(channel);

        /* A server-side streaming **/
        getListContactClient(channel);

        System.out.println("Shutting down channel");
        channel.shutdown();
    }

     
    private static void getContactClient(ManagedChannel channel) {
        AddressBookServiceGrpc.AddressBookServiceBlockingStub stub = AddressBookServiceGrpc.newBlockingStub(channel);

        ContactRequest request = ContactRequest.newBuilder()
            .setName("John")
            .setNumber(111111)
            .setStreet("Alamo St.")
            .addPhoneList("32999999")
            .setPhoneType(PhoneType.WORK)
            .putCoordinates("37.3861", "122.0839")
            .build();

        ContactResponse response = stub.getContact(request);
        System.out.println(response);
    }

    private static void getListContactClient(ManagedChannel channel) {
        AddressBookServiceGrpc.AddressBookServiceBlockingStub stub = AddressBookServiceGrpc.newBlockingStub(channel);

        ContactRequest request = ContactRequest.newBuilder()
            .setName("John")
            .setNumber(111111)
            .setStreet("Alamo St.")
            .addPhoneList("32999999")
            .setPhoneType(PhoneType.WORK)
            .putCoordinates("37.3861", "122.0839")
            .build();

        stub.getListContact(request).forEachRemaining(System.out::println);
    }
}
