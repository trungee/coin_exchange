package com.github.trungee.coin_exchange.pricing_updater.price;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SpotPriceRequest {

    @NonNull
    private final String source;

    @NonNull
    private final String target;

}
