package com.addressbook;

import io.grpc.stub.StreamObserver;

public class AddressBookServiceImpl extends AddressBookServiceGrpc.AddressBookServiceImplBase {
    @Override
    public void getContact(ContactRequest request, StreamObserver<ContactResponse> responseObserver) {
        System.out.println(request);
        responseObserver.onNext(ContactResponse.newBuilder()
                .setResult("Resultado do unário")
                .setCode("200")
                .build());
        // calling onNext multiples times in unary request throws runtime exception
        responseObserver.onCompleted();
    }

    @Override
    public void getListContact(ContactRequest request, StreamObserver<ContactResponse> responseObserver) {
        ContactResponse response = ContactResponse.newBuilder()
            .setResult("Resultado do server stream")
            .setCode("200")
            .build();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             

        for(int i = 0; i < 5; i++){
            responseObserver.onNext(response);
        }

        responseObserver.onCompleted();
    }

    public StreamObserver<ContactRequest> addListContact(StreamObserver<ContactResponse> responseObserver){
        StreamObserver<ContactRequest> requestObserver = new StreamObserver<ContactRequest>() {

            String result = "Hello  \n";
            @Override
            public void onNext(ContactRequest value) {
                result += value.getName() + "\n";
                // ContactResponse response = ContactResponse.newBuilder()
                //         .setResult(result)
                //         .setCode("200")
                //         .build();

                // responseObserver.onNext(response);
            }

            @Override
            public void onError(Throwable t) {
                // do nothing
            }

            @Override
            public void onCompleted() {
                // client is done
                responseObserver.onNext(
                    ContactResponse.newBuilder()
                                .setResult(result)
                                .build()
                );
                responseObserver.onCompleted();
            }
        };

        return requestObserver;
    }

    @Override
    public StreamObserver<ContactRequest> addMultipleContact(StreamObserver<ContactResponse> responseObserver) {
        StreamObserver<ContactRequest> requestObserver = new StreamObserver<ContactRequest>() {
            @Override
            public void onNext(ContactRequest value) {
                String result = "Hello " + value.getName();
                ContactResponse multipleResponse = ContactResponse.newBuilder()
                        .setResult(result)
                        .build();

                responseObserver.onNext(multipleResponse);
            }

            @Override
            public void onError(Throwable t) {
                // do nothing
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };

        return requestObserver;
    }
}