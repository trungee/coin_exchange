package com.trungee.coin_exchange.coin_pricing.service;

import com.trungee.coin_exchange.coin_pricing.dto.PricingResponseDto;
import com.trungee.coin_exchange.coin_pricing.model.PricingRequest;
import com.trungee.coin_exchange.coin_pricing.stream.PricingPayload;
import reactor.core.publisher.Mono;

public interface PricingService {

    void updatePriceAndProfit(PricingPayload pricingPayload);

    Mono<PricingResponseDto> calculate(PricingRequest pricingRequest);
}
