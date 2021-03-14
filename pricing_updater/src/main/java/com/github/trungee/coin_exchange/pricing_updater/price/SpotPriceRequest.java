package com.github.trungee.coin_exchange.pricing_updater.price;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode
public class SpotPriceRequest {

    @NonNull
    private final String base;

    @NonNull
    private final String currency;

}
