package com.kurilenko.covidapi.service.facade;

import com.kurilenko.covidapi.dto.CountryCovidStatsDto;
import com.kurilenko.covidapi.dto.CountryDto;
import com.kurilenko.covidapi.dto.CovidCasesDto;
import com.kurilenko.covidapi.service.CountryService;
import com.kurilenko.covidapi.service.CovidCasesService;
import com.kurilenko.covidapi.service.client.CovidApiClient;
import com.kurilenko.covidapi.service.client.response.CovidDataResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class CovidCasesStatisticFacade {

  private final CovidApiClient covidApiClient;
  private final CovidCasesService covidCasesService;
  private final CountryService countryService;

  @Transactional
  public List<CountryCovidStatsDto> getCovidCasesStatistic(
      List<String> countryCodes,
      LocalDate fromDate,
      LocalDate toDate
  ) {
    List<String> notExistedCountryCodes =
        covidCasesService.findNotExistedCountryCodes(countryCodes);
    if (!notExistedCountryCodes.isEmpty()) {
      Map<String, String> countryMap = countryService.findByCountryCodes(notExistedCountryCodes)
          .stream()
          .collect(Collectors.toMap(CountryDto::getSlug, CountryDto::getCode));
      countryMap
          .entrySet()
          .stream()
          .map(entry -> calculateNewCasesByCountry(entry.getKey(), entry.getValue()))
          .forEach(covidCasesService::saveUsingBatch);
    }

    return covidCasesService
        .getNewCasesStatisticByCountryCodes(countryCodes, fromDate, toDate);
  }

  public List<CovidCasesDto> calculateNewCasesByCountry(String slug, String code) {
    Map<LocalDate, Integer> mapDateByCases = covidApiClient.getCovidDataByCountry(slug)
        .stream()
        .collect(
            Collectors.toMap(key -> key.getDate().toLocalDate(), CovidDataResponse::getCases,
                (r1, r2) -> r2
            ));
    return calculateNewCase(mapDateByCases, code);
  }

  private List<CovidCasesDto> calculateNewCase(Map<LocalDate, Integer> mapDateByCases,
      String countryCode) {
    return mapDateByCases.entrySet()
        .stream()
        .sorted(Map.Entry.comparingByKey())
        .map(entry -> {
          LocalDate prevDay = entry.getKey().minusDays(1);
          return mapDateByCases.containsKey(prevDay) ?
              CovidCasesDto.builder()
                  .countryCode(countryCode)
                  .cases(entry.getValue() - mapDateByCases.get(prevDay))
                  .date(entry.getKey())
                  .build() :
              CovidCasesDto.builder()
                  .countryCode(countryCode)
                  .cases(entry.getValue())
                  .date(entry.getKey())
                  .build();
        })
        .toList();
  }

}
