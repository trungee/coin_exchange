package com.trungee.coin_exchange.coin_pricing.model;

import lombok.*;

@Builder(access = AccessLevel.PUBLIC)
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@EqualsAndHashCode
public class PricingRequest {

    @NonNull
    private final PriceType priceType;

    private final String sourceCurrency;

    private final String targetCurrency;

    private final Double amount;

    public static PricingRequestBuilder of(PriceType priceType) {
        return builder().priceType(priceType);
    }
}
