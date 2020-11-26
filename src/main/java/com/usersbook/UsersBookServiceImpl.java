package com.usersbook;

import io.grpc.stub.StreamObserver;
import java.io.*;

public class UsersBookServiceImpl extends UsersBookServiceGrpc.UsersBookServiceImplBase {
    @Override
    public void getUser(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        System.out.println(request);
        responseObserver.onNext(UserResponse.newBuilder()
                .setResult("Resultado do Unário")
                .setCode("200")
                .build());

        responseObserver.onCompleted();
    }

    @Override
    public void createUser(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        System.out.println(request);
        responseObserver.onNext(UserResponse.newBuilder()
                .setResult("Inserido com Sucesso!")
                .setCode("200")
                .build());

        responseObserver.onCompleted();
    }

    @Override
    public void getListUser(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        UserResponse response = UserResponse.newBuilder()
            .setResult("Resultado do Streaming Server")
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
            }

            @Override
            public void onError(Throwable t) {
                System.out.println(t.toString());
            }

            @Override
            public void onCompleted() {

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
}