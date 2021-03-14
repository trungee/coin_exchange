package com.trungee.coin_exchange.coin_pricing.stream;

import com.trungee.coin_exchange.coin_pricing.service.PricingService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class SimplePriceAndProfitConsumer {

    private final PricingService pricingService;

    public SimplePriceAndProfitConsumer(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @Bean
    public Consumer<DataEvent<PricingPayload>> priceAndProfitConsumer() {
        return event ->  pricingService.updatePriceAndProfit(event.getData());
    }
}
