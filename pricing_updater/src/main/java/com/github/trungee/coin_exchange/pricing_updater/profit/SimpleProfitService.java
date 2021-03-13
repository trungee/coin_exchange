package com.github.trungee.coin_exchange.pricing_updater.profit;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Random;

@Service
public class SimpleProfitService implements ProfitService{

    private final Random random = new Random();
    private final int[] profitsInPercents = {5, 10, 12};

    private int randomChoice(int[] choices) {
        final int randomIndex = random.nextInt(choices.length);
        return choices[randomIndex];
    }

    @Override
    public Mono<Double> currentProfit() {
        return Mono.just(randomChoice(profitsInPercents) / 100d);
    }
}
