package com.kurilenko.covidapi.job;

import com.kurilenko.covidapi.dto.CountryDto;
import com.kurilenko.covidapi.dto.CovidCasesDto;
import com.kurilenko.covidapi.service.CountryService;
import com.kurilenko.covidapi.service.CovidCasesService;
import com.kurilenko.covidapi.service.client.CovidApiClient;
import com.kurilenko.covidapi.service.client.response.CovidDataResponse;
import com.kurilenko.covidapi.service.facade.CovidCasesStatisticFacade;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/*
Job for fill database with new covid cases for not existed countries in database
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FillDBWithCovidCasesJob extends BaseJob {

  private final CountryService countryService;
  private final CovidCasesService covidCasesService;
  private final CovidCasesStatisticFacade covidCasesStatisticFacade;

  @Override
  @Scheduled(cron = "${jobs.fill-db-with-covid-cases.cron}")
  public void schedule() {
    super.schedule();
  }

  @Override
  public boolean toExecute() {
    Map<String, String> availableCountriesMap = countryService.getAll()
        .stream()
        .collect(Collectors.toMap(CountryDto::getCode, CountryDto::getSlug));

    List<String> notExistedCountryCodes =
        covidCasesService.findNotExistedCountryCodes(availableCountriesMap.keySet());

    notExistedCountryCodes
        .forEach(code -> process(availableCountriesMap.get(code), code));

    return true;
  }

  private void process(String slug, String code) {
    try {
      List<CovidCasesDto> newCasesByCountry =
          covidCasesStatisticFacade.calculateNewCasesByCountry(slug, code);
      newCasesByCountry.forEach(covidCasesService::save);
    } catch (Exception e) {
      log.error("Error to persist covid new cases for country: {} {}", code, e.getMessage());
    }
  }

}
