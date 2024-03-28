package ru.iton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import ru.iton.proto.NumberRequest;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Сервис запускает задачу, которая периодически запрашивает у сервера
 * последовательность чисел.
 * В задаче запускает цикл и делается расчет
 * учитывая последовательность полученную от сервера
 */
@Service
public class NumbersService {
    private static final Logger log = LoggerFactory.getLogger(NumbersService.class);
    private final ClientStubController clientStub;
    private final AtomicInteger value = new AtomicInteger();

    public NumbersService(ClientStubController clientStub) {
        this.clientStub = clientStub;
    }

    @Scheduled(fixedDelay = 200000)
    public void startNumberProcess() {
        AtomicInteger inc = new AtomicInteger();
        clientStub.getNumbers(NumberRequest.newBuilder()
                        .setFirstValue(0)
                        .setLastValue(30).build(),
                value);
        Flux.range(0, 50)
                .delayElements(Duration.ofSeconds(1))
                .doOnNext(el -> inc.set(inc.get() + value.getAndSet(0) + 1))
                .subscribe(el -> log.info("Current value {}", inc.get()));
    }
}
