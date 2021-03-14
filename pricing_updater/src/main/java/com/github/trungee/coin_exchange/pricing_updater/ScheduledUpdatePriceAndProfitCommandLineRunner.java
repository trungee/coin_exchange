package com.github.trungee.coin_exchange.pricing_updater;

import com.github.trungee.coin_exchange.pricing_updater.service.PricingUpdaterService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class ScheduledUpdatePriceAndProfitCommandLineRunner implements CommandLineRunner {

    private final PricingUpdaterService pricingUpdaterService;

    public ScheduledUpdatePriceAndProfitCommandLineRunner(PricingUpdaterService pricingUpdaterService) {
        this.pricingUpdaterService = pricingUpdaterService;
    }

    @Override
    public void run(String... args) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
        scheduler.scheduleWithFixedDelay(doUpdatePriceAndProfit(), 0, 1000, TimeUnit.MILLISECONDS);
    }

    private Runnable doUpdatePriceAndProfit() {
        return () -> pricingUpdaterService.update().subscribe();
    }
}
