package com.github.trungee.coin_exchange.pricing_updater.profit;

import reactor.core.publisher.Mono;

public interface ProfitService {
    Mono<Double> currentProfit();
}
