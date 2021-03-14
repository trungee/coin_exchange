package com.github.trungee.coin_exchange.pricing_updater.stream;

import reactor.core.publisher.Mono;

public interface PriceAndProfitSource {

    Mono<Boolean> sendEvent(DataEvent<PricingPayload> dataEvent);
}
