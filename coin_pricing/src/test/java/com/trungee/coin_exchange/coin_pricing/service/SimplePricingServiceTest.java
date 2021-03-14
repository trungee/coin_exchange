package com.trungee.coin_exchange.coin_pricing.service;

import com.trungee.coin_exchange.coin_pricing.dto.PricingResponseDto;
import com.trungee.coin_exchange.coin_pricing.model.PriceType;
import com.trungee.coin_exchange.coin_pricing.model.PricingRequest;
import com.trungee.coin_exchange.coin_pricing.stream.PricingPayload;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SimplePricingServiceTest {

    private static final String CURRENCY_BTC = "BTC";
    private static final String CURRENCY_NZD = "NZD";
    private static final Double PROFIT_FACTOR = 0.05;
    private static final BigDecimal BTC_NZD_SPOT_PRICE = BigDecimal.valueOf(85615.9763);
    public static final PricingPayload PRICING_PAYLOAD_BTC_NZD = PricingPayload.builder()
            .currency(CURRENCY_NZD)
            .spotPrice(BTC_NZD_SPOT_PRICE)
            .profitFactor(PROFIT_FACTOR)
            .build();

    private SimplePricingService simplePricingService;

    @BeforeEach
    void setUp() {
        this.simplePricingService = new SimplePricingService();
    }

    @Test
    void updatePriceAndProfit() {
        simplePricingService.updatePriceAndProfit(PRICING_PAYLOAD_BTC_NZD);
        PricingRequest pricingRequest = PricingRequest.of(PriceType.BUY)
                .sourceCurrency(CURRENCY_BTC)
                .targetCurrency(CURRENCY_NZD)
                .amount(1d)
                .build();
        PricingResponseDto pricingResponseDto = simplePricingService.calculate(pricingRequest).block();
        assertEquals(PROFIT_FACTOR, pricingResponseDto.getProfitFactor());
        assertEquals(BTC_NZD_SPOT_PRICE, pricingResponseDto.getSpotPrice());
    }

    @Test
    void calculate_buyingPrice() {
        PricingRequest pricingRequest = PricingRequest.of(PriceType.BUY)
                .sourceCurrency(CURRENCY_BTC)
                .targetCurrency(CURRENCY_NZD)
                .amount(1d)
                .build();
        simplePricingService.updatePriceAndProfit(PRICING_PAYLOAD_BTC_NZD);
        BigDecimal totalPrice = simplePricingService.calculate(pricingRequest).block().getTotalPrice();
        assertThat(totalPrice, Matchers.comparesEqualTo(BigDecimal.valueOf(89896.7751150)));
    }

    @Test
    void calculate_sellingPrice() {
        PricingRequest pricingRequest = PricingRequest.of(PriceType.SELL)
                .sourceCurrency(CURRENCY_BTC)
                .targetCurrency(CURRENCY_NZD)
                .amount(1d)
                .build();
        simplePricingService.updatePriceAndProfit(PRICING_PAYLOAD_BTC_NZD);
        BigDecimal totalPrice = simplePricingService.calculate(pricingRequest).block().getTotalPrice();
        assertThat(totalPrice, Matchers.comparesEqualTo(BigDecimal.valueOf(81539.02510)));
    }

}