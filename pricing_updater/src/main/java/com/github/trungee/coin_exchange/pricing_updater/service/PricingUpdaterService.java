package com.github.trungee.coin_exchange.pricing_updater.service;

import reactor.core.publisher.Mono;

public interface PricingUpdaterService {

    Mono<Boolean> update();
}
