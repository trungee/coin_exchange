package com.trungee.coin_exchange.coin_pricing.stream;

import com.trungee.coin_exchange.coin_pricing.service.PricingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.support.MessageBuilder;

import java.math.BigDecimal;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class SimplePriceAndProfitConsumerTest {

    private static final String CURRENCY_NZD = "NZD";
    private static final Double PROFIT_FACTOR = 0.05;

    @Autowired
    InputDestination input;

    @MockBean
    PricingService pricingService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void priceAndProfitConsumer_priceAndProfitChanged_shouldUpdatePriceAndProfit() {
        final PricingPayload pricingPayload = PricingPayload.builder()
                .spotPrice(BigDecimal.TEN)
                .currency(CURRENCY_NZD)
                .profitFactor(PROFIT_FACTOR)
                .build();
        input.send(MessageBuilder.withPayload(DataEvent.builder()
                .data(pricingPayload)
                .build())
                .build());
        verify(pricingService, times(1)).updatePriceAndProfit(pricingPayload);
    }
}