package com.github.trungee.coin_exchange.pricing_updater.stream;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
@Builder
public class PricingPayload {

    private String currency;

    private BigDecimal spotPrice;

    private Double profitFactor;
}
