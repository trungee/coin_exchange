package com.github.trungee.coin_exchange.pricing_updater.service;

import reactor.core.publisher.Mono;

public interface ThirdPartyClient {

    <T> Mono<T> get(String path, Class<T> desiredClass, Object... parameters);
}
