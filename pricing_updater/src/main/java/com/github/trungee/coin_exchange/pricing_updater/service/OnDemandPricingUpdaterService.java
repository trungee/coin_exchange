package com.github.trungee.coin_exchange.pricing_updater.service;

import com.github.trungee.coin_exchange.pricing_updater.price.SpotPriceRequest;
import com.github.trungee.coin_exchange.pricing_updater.price.PriceService;
import com.github.trungee.coin_exchange.pricing_updater.profit.ProfitService;
import com.github.trungee.coin_exchange.pricing_updater.stream.DataEvent;
import com.github.trungee.coin_exchange.pricing_updater.stream.PriceAndProfitSource;
import com.github.trungee.coin_exchange.pricing_updater.stream.PricingPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@Slf4j
public class OnDemandPricingUpdaterService implements PricingUpdaterService{

    private final PriceAndProfitSource source;
    private final PriceService priceService;
    private final ProfitService profitService;

    public OnDemandPricingUpdaterService(PriceAndProfitSource source,
                                         PriceService priceService,
                                         ProfitService profitService) {
        this.source = source;
        this.priceService = priceService;
        this.profitService = profitService;
    }

    @Override
    public Mono<Void> update() {
        Mono<BigDecimal> spotPrice = priceService.retrieveSpotPrice(new SpotPriceRequest("BTC", "NZD"));
        Mono<Double> profit = profitService.currentProfit();
        return Mono.zipDelayError(spotPrice, profit)
                .doOnSuccess(tuple2 ->
                        source.sendEvent(DataEvent.<PricingPayload> builder()
                                .data(new PricingPayload(tuple2.getT1(), tuple2.getT2()))
                                .createdAt(Instant.now())
                                .build()))
                .doOnError(error -> log.error(error.getMessage()))
                .then();
    }
}
