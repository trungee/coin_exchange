package com.github.trungee.coin_exchange.pricing_updater.service;

import com.github.trungee.coin_exchange.pricing_updater.price.PriceService;
import com.github.trungee.coin_exchange.pricing_updater.price.SpotPriceRequest;
import com.github.trungee.coin_exchange.pricing_updater.profit.ProfitService;
import com.github.trungee.coin_exchange.pricing_updater.stream.DataEvent;
import com.github.trungee.coin_exchange.pricing_updater.stream.PriceAndProfitSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OnDemandPricingUpdaterServiceTest {

    private static final String CURRENCY_NZD = "NZD";
    private static final String CURRENCY_BTC = "BTC";
    private OnDemandPricingUpdaterService onDemandPricingUpdaterService;
    private PriceAndProfitSource source;
    private PriceService priceService;
    private ProfitService profitService;
    private final SpotPriceRequest nzdSpotPriceRequest = new SpotPriceRequest(CURRENCY_BTC, CURRENCY_NZD);

    @BeforeEach
    void setUp() {
        source = mock(PriceAndProfitSource.class);
        priceService = mock(PriceService.class);
        profitService = mock(ProfitService.class);
        when(source.sendEvent(any(DataEvent.class))).thenReturn(Mono.just(true));
        when(priceService.retrieveSpotPrice(nzdSpotPriceRequest)).thenReturn(Mono.just(BigDecimal.TEN));
        when(profitService.currentProfit()).thenReturn(Mono.just(5d));

        onDemandPricingUpdaterService = new OnDemandPricingUpdaterService(source, priceService, profitService);
    }

    @Test
    public void update_priceAndProfitFactorChanged_shouldSendUpdateEvent() {
        StepVerifier.create(onDemandPricingUpdaterService.update())
                .expectNext(true)
                .verifyComplete();
        verify(source, times(1)).sendEvent(any(DataEvent.class));
    }

    @Test
    public void update_priceChanged_shouldSendUpdateEvent() {
        when(priceService.retrieveSpotPrice(nzdSpotPriceRequest))
                .thenReturn(Mono.just(BigDecimal.TEN), Mono.just(BigDecimal.ONE));
        onDemandPricingUpdaterService.update().block();
        StepVerifier.create(onDemandPricingUpdaterService.update())
                .expectNext(true)
                .verifyComplete();
        verify(source, times(2)).sendEvent(any(DataEvent.class));
    }

    @Test
    public void update_spotPriceChanged_shouldSendUpdateEvent() {
        when(profitService.currentProfit())
                .thenReturn(Mono.just(5d), Mono.just(10d));
        onDemandPricingUpdaterService.update().block();
        StepVerifier.create(onDemandPricingUpdaterService.update())
                .expectNext(true)
                .verifyComplete();
        verify(source, times(2)).sendEvent(any(DataEvent.class));
    }

    @Test
    public void update_spotPriceAndProfitNotChanged_shouldNotSendUpdateEvent() {
        onDemandPricingUpdaterService.update().block();
        StepVerifier.create(onDemandPricingUpdaterService.update())
                .verifyComplete();
        verify(source, times(1)).sendEvent(any(DataEvent.class));
    }

}