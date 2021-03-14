package com.github.trungee.coin_exchange.pricing_updater.stream;

import com.github.trungee.coin_exchange.pricing_updater.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


@Component
@Slf4j
public class SimplePriceAndProfitSource implements PriceAndProfitSource {

    private static final String PRODUCER_BINDING_NAME = "priceAndProficChanged";

    private final StreamBridge streamBridge;

    public SimplePriceAndProfitSource(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    @Override
    public Mono<Boolean> sendEvent(DataEvent<PricingPayload> dataEvent) {
        final Mono<Boolean> result;
        log.info("Sending event to queue: {} - data: {}", PRODUCER_BINDING_NAME, dataEvent);
        if (streamBridge.send(PRODUCER_BINDING_NAME, dataEvent)) {
            result = Mono.empty();
            log.info("Send event success");
        } else {
            result = Mono.error(new BadRequestException("Failed to send event"));
        }
        return result;
    }

}
