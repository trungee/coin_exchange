package com.trungee.coin_exchange.coin_pricing.service;

import com.trungee.coin_exchange.coin_pricing.dto.PricingResponseDto;
import com.trungee.coin_exchange.coin_pricing.model.PricingRequest;
import com.trungee.coin_exchange.coin_pricing.stream.PricingPayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SimplePricingService implements PricingService {

    private final ConcurrentHashMap<String, BigDecimal> currentCurrencyPrice;
    private Double currentProfitFactor;

    public SimplePricingService() {
        this.currentCurrencyPrice = new ConcurrentHashMap<>();
        this.currentProfitFactor = null;
    }

    @Override
    public void updatePriceAndProfit(PricingPayload pricingPayload) {
        log.info("Price and profit factor changed: {}", pricingPayload);
        currentCurrencyPrice.put(pricingPayload.getCurrency(), pricingPayload.getSpotPrice());
        currentProfitFactor = pricingPayload.getProfitFactor();
    }

    @Override
    public Mono<PricingResponseDto> calculate(PricingRequest pricingRequest) {
        final BigDecimal total;
        final BigDecimal currentSpotPrice = currentCurrencyPrice.get(pricingRequest.getTargetCurrency());
        final Double currentProfitFactor = this.currentProfitFactor;
        switch (pricingRequest.getPriceType()) {
            case BUY:
                total = buyingPrice(currentSpotPrice, currentProfitFactor, pricingRequest.getAmount());
                break;
            case SELL:
            default:
                total = sellingPrice(currentSpotPrice, currentProfitFactor, pricingRequest.getAmount());
                break;
        }
        return Mono.just(PricingResponseDto.builder()
                .spotPrice(currentSpotPrice)
                .profitFactor(currentProfitFactor)
                .amount(pricingRequest.getAmount())
                .totalPrice(total)
                .build());
    }

    private BigDecimal buyingPrice(BigDecimal spotPrice, Double profitFactor, Double amount) {
        return spotPrice.multiply(BigDecimal.valueOf(1 + profitFactor))
                .multiply(BigDecimal.valueOf(amount));
    }

    private BigDecimal sellingPrice(BigDecimal spotPrice, Double profitFactor, Double amount) {
        return spotPrice.divide(BigDecimal.valueOf(1 + profitFactor), RoundingMode.CEILING)
                .multiply(BigDecimal.valueOf(amount));
    }
}
