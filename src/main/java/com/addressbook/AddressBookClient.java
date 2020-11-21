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
        AddressBookServiceGrpc.AddressBookServiceBlockingStub stub =  AddressBookServiceGrpc.newBlockingStub(channel);

        ContactResponse response = stub.getContact(ContactRequest.newBuilder()
                .setName("John")
                .setNumber(111111)
                .setStreet("Alamo St.")
                .addPhoneList("32999999")
                .setPhoneType(PhoneType.WORK)
                .putCoordinates("37.3861", "122.0839")
                .build());
        System.out.println(response);
    }
}
