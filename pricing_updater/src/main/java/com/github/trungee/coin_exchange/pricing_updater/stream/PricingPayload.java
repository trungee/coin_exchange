package com.github.trungee.coin_exchange.pricing_updater.stream;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class PricingPayload {

    private BigDecimal spotPrice;

    private Double profitFactor;
}
