package com.github.trungee.coin_exchange.pricing_updater.price;

import reactor.core.publisher.Mono;

import java.math.BigDecimal;

public interface PriceService {

    Mono<BigDecimal> retrieveSpotPrice(SpotPriceRequest spotPriceRequest);
}
