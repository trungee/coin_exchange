package com.github.trungee.coin_exchange.pricing_updater.service;

import com.github.trungee.coin_exchange.pricing_updater.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Component
public class WebClientThirdPartyClient implements ThirdPartyClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebClientThirdPartyClient.class);
    private final WebClient webClient;

    @Autowired
    public WebClientThirdPartyClient(WebClient.Builder webClientBuiler) {
        this.webClient = webClientBuiler.build();
    }

    @Override
    public <T> Mono<T> get(String url, Class<T> desiredClass, Object... parameters) {
        return webClient.get()
                .uri(url, parameters)
                .retrieve()
                .bodyToMono(desiredClass)
                .onErrorMap(WebClientResponseException.class, this::handleException);
    }

    private Throwable handleException(Throwable ex) {
        final Throwable exception;
        if (!(ex instanceof WebClientResponseException)) {
            LOGGER.error("Unexpected error: {}", ex.getMessage());
            exception = ex;
        } else {
            WebClientResponseException webClientResponseException = (WebClientResponseException)ex;
            switch (webClientResponseException.getStatusCode()) {
                case NOT_FOUND:
                    exception = new RuntimeException(webClientResponseException.getMessage());
                    break;
                case BAD_REQUEST:
                    exception = new BadRequestException(webClientResponseException.getMessage());
                    break;
                default:
                    exception = webClientResponseException;
            }
        }
        return exception;
    }
}
