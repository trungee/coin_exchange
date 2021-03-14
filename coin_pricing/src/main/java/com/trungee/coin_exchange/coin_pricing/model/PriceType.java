package com.trungee.coin_exchange.coin_pricing.model;

import lombok.Getter;

public enum PriceType {
    BUY("buy"), SELL("sell");

    @Getter
    private final String type;

    PriceType(String type) {
        this.type = type;
    }
}
