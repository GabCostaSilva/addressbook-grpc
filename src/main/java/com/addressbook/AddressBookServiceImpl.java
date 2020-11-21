package com.addressbook;

import io.grpc.stub.StreamObserver;

public class AddressBookServiceImpl extends AddressBookServiceGrpc.AddressBookServiceImplBase {
    @Override
    public void getContact(ContactRequest request, StreamObserver<ContactResponse> responseObserver) {
        System.out.println(request);
        responseObserver.onNext(ContactResponse.newBuilder()
                    .setCode("200")
                .build());
        // calling onNext multiples times in unary request throws runtime exception
        responseObserver.onCompleted();
    }
}