package com.usersbook;

import io.grpc.stub.StreamObserver;
import java.io.*;

public class UsersBookServiceImpl extends UsersBookServiceGrpc.UsersBookServiceImplBase {
    @Override
    public void getUser(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        System.out.println(request);
        responseObserver.onNext(UserResponse.newBuilder()
                .setResult("Resultado do unário")
                .setCode("200")
                .build());
        // calling onNext multiples times in unary request throws runtime exception
        responseObserver.onCompleted();
    }

    @Override
    public void createUser(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        System.out.println(request);
        responseObserver.onNext(UserResponse.newBuilder()
                .setResult("Resultado do unário")
                .setCode("200")
                .build());
        
        // calling onNext multiples times in unary request throws runtime exception
        responseObserver.onCompleted();
    }

    @Override
    public void getListUser(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        UserResponse response = UserResponse.newBuilder()
            .setResult("Resultado do server stream")
            .setCode("200")
            .build();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             

        for(int i = 0; i < 5; i++){
            responseObserver.onNext(response);
        }

        responseObserver.onCompleted();
    }

    public StreamObserver<UserRequest> addListUser(StreamObserver<UserResponse> responseObserver){
        StreamObserver<UserRequest> requestObserver = new StreamObserver<UserRequest>() {

            String result = "Hello  \n";
            @Override
            public void onNext(UserRequest value) {
                result += value.getUser().getName() + "\n";
                // UserResponse response = UserResponse.newBuilder()
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
                    UserResponse.newBuilder()
                        .setResult(result)
                        .build()
                );
                responseObserver.onCompleted();
            }
        };

        return requestObserver;
    }

    // @Override
    // public StreamObserver<UserRequest> addMultipleContact(StreamObserver<UserResponse> responseObserver) {
    //     StreamObserver<UserRequest> requestObserver = new StreamObserver<UserRequest>() {
    //         @Override
    //         public void onNext(UserRequest value) {
    //             String result = "Hello " + value.getName();
    //             UserResponse multipleResponse = UserResponse.newBuilder()
    //                     .setResult(result)
    //                     .build();

    //             responseObserver.onNext(multipleResponse);
    //         }

    //         @Override
    //         public void onError(Throwable t) {
    //             // do nothing
    //         }

    //         @Override
    //         public void onCompleted() {
    //             responseObserver.onCompleted();
    //         }
    //     };

    //     return requestObserver;
    // }
}