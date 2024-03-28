package ru.iton;

import io.grpc.ManagedChannel;

import io.grpc.ManagedChannelBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.iton.proto.ReactorNumbersGrpc;

@Configuration
@EnableScheduling
public class GrpcClientConfig {

    @Value("${host}")
    private String host;

    @Value("${port}")
    private int port;

    @Bean(destroyMethod = "shutdown")
    public ManagedChannel simpleManagedChannel() {
        return ManagedChannelBuilder.forAddress(host, port)
                .usePlaintext()
                .build();
    }

    @Bean
    public ReactorNumbersGrpc.ReactorNumbersStub nonBlockingStub(ManagedChannel simpleManagedChannel){
        return ReactorNumbersGrpc.newReactorStub(simpleManagedChannel);
    }
}
