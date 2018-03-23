package com.monochromeroad.flr.api.greeting;

import org.lognet.springboot.grpc.GRpcService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.grpc.stub.StreamObserver;

@GRpcService
public class GreetingService extends GreetingServiceGrpc.GreetingServiceImplBase {
    @Override
    public void greet(Greeting request, StreamObserver<Greeting> responseObserver) {
        Greeting reply = Greeting.newBuilder().
            setResponseType(Greeting.Type.NIGHT).
            setRequestType(Greeting.Type.NIGHT).
            setMessage("Hello, " + request.getName()).
            build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }
}
