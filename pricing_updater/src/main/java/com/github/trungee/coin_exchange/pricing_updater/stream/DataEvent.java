package com.github.trungee.coin_exchange.pricing_updater.stream;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Instant;

@Builder
@Getter
@ToString
public class DataEvent<T> {

    private T data;
    private Instant createdAt;
}
