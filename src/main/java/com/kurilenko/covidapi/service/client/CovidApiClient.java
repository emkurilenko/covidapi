package com.kurilenko.covidapi.service.client;

import com.kurilenko.covidapi.service.client.response.CountryResponse;
import com.kurilenko.covidapi.service.client.response.CovidDataResponse;
import com.kurilenko.covidapi.service.client.response.SummaryResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@RequiredArgsConstructor
public class CovidApiClient {

  public static final String COUNTRIES_PATH = "/countries";
  public static final String COUNTRY_STATUS_CONFIRMED_PATH = "/country/{countrySlug}/status/confirmed";
  public static final String SUMMARY_PATH = "/summary";


  private final WebClient covidApiWebClient;

  public List<CountryResponse> getAllCountries() {
    return covidApiWebClient.get()
        .uri(COUNTRIES_PATH)
        .retrieve()
        .bodyToFlux(CountryResponse.class)
        .collectList()
        .block();
  }

  public List<CovidDataResponse> getCovidDataByCountry(String countrySlug) {
    return covidApiWebClient.get()
        .uri(uriBuilder ->
            uriBuilder.path(COUNTRY_STATUS_CONFIRMED_PATH)
                .build(countrySlug))
        .retrieve()
        .bodyToFlux(CovidDataResponse.class)
        .collectList()
        .block();
  }

  public SummaryResponse getSummary() {
    return covidApiWebClient.get()
        .uri(SUMMARY_PATH)
        .retrieve()
        .bodyToMono(SummaryResponse.class)
        .block();
  }
}
