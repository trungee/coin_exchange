package com.github.trungee.coin_exchange.pricing_updater.stream;

import reactor.core.publisher.Mono;

public interface PriceAndProfitSource {

    Mono<Void> sendEvent(DataEvent<PricingPayload> dataEvent);
}
