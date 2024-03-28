package ru.iton;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import ru.iton.proto.NumberRequest;
import ru.iton.proto.ReactorNumbersGrpc;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ClientStubController {

    private final ReactorNumbersGrpc.ReactorNumbersStub reactorNumbersStub;

    public ClientStubController(ReactorNumbersGrpc.ReactorNumbersStub reactorNumbersStub) {
        this.reactorNumbersStub = reactorNumbersStub;
    }

    public void getNumbers(NumberRequest numberRequest, AtomicInteger value) {
        reactorNumbersStub.getNumbers(Mono.just(numberRequest))
                .log()
                .doOnNext(el -> value.set(el.getValue()))
                .subscribe();
    }
}
