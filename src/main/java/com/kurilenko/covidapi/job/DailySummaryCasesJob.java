package com.kurilenko.covidapi.job;

import com.kurilenko.covidapi.dto.CovidCasesDto;
import com.kurilenko.covidapi.service.CovidCasesService;
import com.kurilenko.covidapi.service.client.CovidApiClient;
import com.kurilenko.covidapi.service.client.response.SummaryResponse;
import com.kurilenko.covidapi.service.client.response.SummaryResponse.CountrySummaryDTO;
import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/*
Job for update summary cases by current day
 */
@Component
@RequiredArgsConstructor
public class DailySummaryCasesJob extends BaseJob {

  private final CovidCasesService covidCasesService;
  private final CovidApiClient covidApiClient;

  @Override
  @Scheduled(cron = "${jobs.daily-summary-cases.cron}")
  public void schedule() {
    super.schedule();
  }

  @Override
  @Transactional
  public boolean toExecute() {
    SummaryResponse summary = covidApiClient.getSummary();
    LocalDate date = summary.getDate();
    Map<String, Integer> countryNewConfirmedCasesMap = summary.getCountries()
        .stream()
        .collect(Collectors.toMap(CountrySummaryDTO::getCountryCode,
            CountrySummaryDTO::getNewConfirmed));

    covidCasesService.findExistedCountryCodes()
        .forEach(countryCode ->
            covidCasesService.updateNewCasesByDate(CovidCasesDto.builder()
                .cases(countryNewConfirmedCasesMap.get(countryCode))
                .date(date)
                .countryCode(countryCode)
                .build())
        );

    return true;
  }

}
