package ru.iton.services.grpc;

import com.google.protobuf.Int32Value;
import net.devh.boot.grpc.server.service.GrpcService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import ru.iton.proto.NumberRequest;
import ru.iton.proto.ReactorNumbersGrpc;

import java.time.Duration;

/**
 * Сервис возвращает стрим последовательности чисел
 */
@GrpcService
public class GrpcNumberService extends ReactorNumbersGrpc.NumbersImplBase {
    @Override
    public Flux<Int32Value> getNumbers(Mono<NumberRequest> request) {
        return request.flatMapMany(el -> Flux.range(el.getFirstValue(), el.getLastValue())
                                .takeWhile(i -> i <= el.getLastValue()))
                .log()
                .map(Int32Value::of)
                .delayElements(Duration.ofSeconds(2));
    }
}
