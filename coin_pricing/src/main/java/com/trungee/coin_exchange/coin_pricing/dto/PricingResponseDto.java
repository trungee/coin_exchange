package com.trungee.coin_exchange.coin_pricing.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonAutoDetect
@Builder
public class PricingResponseDto {

    private String id;
    private BigDecimal spotPrice;
    private Double profitFactor;
    private Double amount;
    private BigDecimal totalPrice;
}
