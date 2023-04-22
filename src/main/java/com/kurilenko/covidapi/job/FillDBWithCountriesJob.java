package com.kurilenko.covidapi.job;

import com.kurilenko.covidapi.dto.CountryDto;
import com.kurilenko.covidapi.service.CountryService;
import com.kurilenko.covidapi.service.client.CovidApiClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/*
Job for fill database with countries
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FillDBWithCountriesJob extends BaseJob {

  private final CountryService countryService;
  private final CovidApiClient covidApiClient;

  @Override
  @EventListener(ApplicationReadyEvent.class)
  public void schedule() {
    super.schedule();
  }

  @Override
  @Transactional
  public boolean toExecute() {
    boolean databaseIsEmpty = countryService.getAll().isEmpty();
    if (!databaseIsEmpty) {
      return true;
    }
    covidApiClient.getAllCountries()
        .stream()
        .map(item -> CountryDto.builder()
            .name(item.getCountry())
            .code(item.getIso2())
            .slug(item.getSlug())
            .build())
        .forEach(countryService::save);

    return true;
  }

}
