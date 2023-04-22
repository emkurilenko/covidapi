package com.kurilenko.covidapi.config;

import com.kurilenko.covidapi.config.properties.CovidApiProperties;
import com.kurilenko.covidapi.config.properties.WebClientRetryProperties;
import com.kurilenko.covidapi.config.properties.WebClientRetryProperties.BackoffProperty;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;
import reactor.util.retry.RetryBackoffSpec;

@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

  private final CovidApiProperties properties;
  private final WebClientRetryProperties webClientRetryProperties;

  @Bean
  public WebClient covidApiWebClient() {
    return WebClient.builder()
        .baseUrl(properties.getHost())
        .filter(buildRetryExchangeFilterFunction())
        .build();
  }

  private ExchangeFilterFunction buildRetryExchangeFilterFunction() {
    return (request, next) -> next.exchange(request)
        .flatMap(clientResponse -> Mono.just(clientResponse)
            .filter(response -> clientResponse.statusCode().isError())
            .flatMap(response -> clientResponse.createException())
            .flatMap(Mono::error)
            .thenReturn(clientResponse))
        .retryWhen(retryWhenTooManyRequests());
  }

  private RetryBackoffSpec retryWhenTooManyRequests() {
    BackoffProperty minBackoff = webClientRetryProperties.getMinBackoff();
    Duration minBackoffDuration = Duration.of(minBackoff.getAmount(),
        ChronoUnit.valueOf(minBackoff.getUnit()));
    return Retry.backoff(webClientRetryProperties.getMaxAttempts(), minBackoffDuration)
        .filter(this::isTooManyRequestsException)
        .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> retrySignal.failure());
  }

  private boolean isTooManyRequestsException(final Throwable throwable) {
    return throwable instanceof WebClientResponseException.TooManyRequests;
  }

}
