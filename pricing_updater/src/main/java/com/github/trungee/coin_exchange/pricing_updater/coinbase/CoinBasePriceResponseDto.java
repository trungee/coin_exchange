package com.github.trungee.coin_exchange.pricing_updater.coinbase;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CoinBasePriceResponseDto {

    private Data data;

    @lombok.Data
    public static class Data {

        private String base;
        private String currency;
        private BigDecimal amount;
    }
}
