package com.github.trungee.coin_exchange.pricing_updater.service;

import com.github.trungee.coin_exchange.pricing_updater.price.PriceService;
import com.github.trungee.coin_exchange.pricing_updater.price.SpotPriceRequest;
import com.github.trungee.coin_exchange.pricing_updater.profit.ProfitService;
import com.github.trungee.coin_exchange.pricing_updater.stream.DataEvent;
import com.github.trungee.coin_exchange.pricing_updater.stream.PriceAndProfitSource;
import com.github.trungee.coin_exchange.pricing_updater.stream.PricingPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Predicate;

@Service
@Slf4j
public class OnDemandPricingUpdaterService implements PricingUpdaterService{

    public static final String DEFAULT_CURRENCY = "NZD";
    public static final String BASE_BTC = "BTC";
    private final PriceAndProfitSource source;
    private final PriceService priceService;
    private final ProfitService profitService;
    private final ConcurrentMap<String, BigDecimal> currentCurrencyPrice = new ConcurrentHashMap<>();
    private Double currentProfitFactor;

    private final Predicate<PricingPayload> spotPriceChanged = payload ->
            isSpotPriceChanged(payload.getCurrency(), payload.getSpotPrice());

    private final Predicate<PricingPayload> profitFactorChanged = payload ->
            !payload.getProfitFactor().equals(currentProfitFactor);

    public OnDemandPricingUpdaterService(PriceAndProfitSource source,
                                         PriceService priceService,
                                         ProfitService profitService) {
        this.source = source;
        this.priceService = priceService;
        this.profitService = profitService;
        this.currentProfitFactor = null;
    }

    @Override
    public Mono<Boolean> update() {
        Mono<BigDecimal> spotPrice = priceService.retrieveSpotPrice(new SpotPriceRequest(BASE_BTC, DEFAULT_CURRENCY));
        Mono<Double> profit = profitService.currentProfit();
        return Mono.zipDelayError(spotPrice, profit)
                .flatMap(tuple2 ->
                    sendEventIfDataChanged(PricingPayload.builder()
                            .currency(DEFAULT_CURRENCY)
                            .spotPrice(tuple2.getT1())
                            .profitFactor(tuple2.getT2())
                            .build()))
                .doOnError(error -> log.error(error.getMessage()));

    }

    private boolean isSpotPriceChanged(String currency, BigDecimal newAmount) {
        Objects.requireNonNull(newAmount);
        return !Objects.equals(newAmount, currentCurrencyPrice.get(currency));
    }

    private Mono<Boolean> sendEventIfDataChanged(PricingPayload pricingPayload) {
        final Mono<Boolean> result;
        if (spotPriceChanged.or(profitFactorChanged).test(pricingPayload)) {
            result = source.sendEvent(DataEvent.<PricingPayload>builder()
                    .data(pricingPayload)
                    .createdAt(Instant.now())
                    .build());
        } else {
            result = Mono.empty();
        }
        currentCurrencyPrice.put(pricingPayload.getCurrency(), pricingPayload.getSpotPrice());
        currentProfitFactor = pricingPayload.getProfitFactor();
        return result;
    }
}
