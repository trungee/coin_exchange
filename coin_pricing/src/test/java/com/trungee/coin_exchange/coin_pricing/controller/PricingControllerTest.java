package com.trungee.coin_exchange.coin_pricing.controller;

import com.trungee.coin_exchange.coin_pricing.dto.PricingResponseDto;
import com.trungee.coin_exchange.coin_pricing.model.PriceType;
import com.trungee.coin_exchange.coin_pricing.model.PricingRequest;
import com.trungee.coin_exchange.coin_pricing.service.PricingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureWebTestClient
@Import(TestChannelBinderConfiguration.class)
class PricingControllerTest {

    private static final String CURRENCY_BTC = "BTC";
    private static final String CURRENCY_NZD = "NZD";

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PricingService pricingService;

    private final PricingResponseDto pricingResponseDtoForBuy;
    private final PricingResponseDto pricingResponseDtoForSell;
    private final PricingRequest pricingRequestForBuy;
    private final PricingRequest pricingRequestForSell;

    public PricingControllerTest() {
        pricingRequestForBuy = PricingRequest.of(PriceType.BUY)
                .sourceCurrency(CURRENCY_BTC)
                .targetCurrency(CURRENCY_NZD)
                .amount(1.0)
                .build();
        pricingRequestForSell = PricingRequest.of(PriceType.SELL)
                .sourceCurrency(CURRENCY_BTC)
                .targetCurrency(CURRENCY_NZD)
                .amount(1.0)
                .build();
        pricingResponseDtoForBuy = PricingResponseDto.builder()
                .spotPrice(BigDecimal.valueOf(100))
                .amount(1.0)
                .profitFactor(0.05)
                .totalPrice(BigDecimal.valueOf(105))
                .build();
        pricingResponseDtoForSell = PricingResponseDto.builder()
                .spotPrice(BigDecimal.valueOf(100))
                .amount(1.0)
                .profitFactor(0.05)
                .totalPrice(BigDecimal.valueOf(95))
                .build();
    }

    @BeforeEach
    void setUp() {
        when(pricingService.calculate(pricingRequestForBuy))
                .thenReturn(Mono.just(pricingResponseDtoForBuy));
        when(pricingService.calculate(pricingRequestForSell))
                .thenReturn(Mono.just(pricingResponseDtoForSell));
    }

    @Test
    void getBuyingPrice() {
        webTestClient.get()
                .uri("/api/prices/BTC-NZD/buy?amount=1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(PricingResponseDto.class)
                .isEqualTo(pricingResponseDtoForBuy);
    }

    @Test
    void getSellingPrice() {
        webTestClient.get()
                .uri("/api/prices/BTC-NZD/sell?amount=1")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(PricingResponseDto.class)
                .isEqualTo(pricingResponseDtoForSell);
    }
}