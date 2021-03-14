package com.trungee.coin_exchange.coin_pricing.controller;

import com.trungee.coin_exchange.coin_pricing.dto.PricingResponseDto;
import com.trungee.coin_exchange.coin_pricing.model.PriceType;
import com.trungee.coin_exchange.coin_pricing.model.PricingRequest;
import com.trungee.coin_exchange.coin_pricing.service.PricingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/prices")
public class PricingController extends BaseController {

    private final PricingService pricingService;

    public PricingController(PricingService pricingService) {
        this.pricingService = pricingService;
    }

    @GetMapping("/{sourceCurrency}-{targetCurrency}/buy")
    public Mono<ResponseEntity<PricingResponseDto>> getBuyingPrice(@PathVariable("sourceCurrency") String sourceCurrency,
                                                                @PathVariable("targetCurrency") String targetCurrent,
                                                                @RequestParam("amount") Double amount) {
        return pricingService.calculate(PricingRequest.of(PriceType.BUY)
                .sourceCurrency(sourceCurrency)
                .targetCurrency(targetCurrent)
                .amount(amount)
                .build())
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{sourceCurrency}-{targetCurrency}/sell")
    public Mono<ResponseEntity<PricingResponseDto>> getSellingPrice(@PathVariable("sourceCurrency") String sourceCurrency,
                                                                   @PathVariable("targetCurrency") String targetCurrent,
                                                                   @RequestParam("amount") Double amount) {
        return pricingService.calculate(PricingRequest.of(PriceType.SELL)
                .sourceCurrency(sourceCurrency)
                .targetCurrency(targetCurrent)
                .amount(amount)
                .build())
                .map(ResponseEntity::ok);
    }
}
