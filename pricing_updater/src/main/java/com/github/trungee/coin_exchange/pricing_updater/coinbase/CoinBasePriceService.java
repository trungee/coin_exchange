package com.github.trungee.coin_exchange.pricing_updater.coinbase;

import com.github.trungee.coin_exchange.pricing_updater.price.PriceService;
import com.github.trungee.coin_exchange.pricing_updater.price.SpotPriceRequest;
import com.github.trungee.coin_exchange.pricing_updater.service.ThirdPartyClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
public class CoinBasePriceService implements PriceService {

    private static final String SPOT_PATH = "/prices/{source}-{target}/spot";
    private final ThirdPartyClient thirdPartyClient;

    public CoinBasePriceService(ThirdPartyClient thirdPartyClient) {
        this.thirdPartyClient = thirdPartyClient;
    }

    @Override
    public Mono<BigDecimal> retrieveSpotPrice(SpotPriceRequest spotPriceRequest) {
        return thirdPartyClient.get(SPOT_PATH, CoinBasePriceResponseDto.class,
                spotPriceRequest.getBase(), spotPriceRequest.getCurrency())
                .map(dto -> dto.getData().getAmount());
    }
}
