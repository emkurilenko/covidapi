package com.kurilenko.covidapi.controller;

import com.kurilenko.covidapi.dto.CountryCovidStatsDto;
import com.kurilenko.covidapi.service.CovidCasesService;
import com.kurilenko.covidapi.service.facade.CovidCasesStatisticFacade;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class CovidStatsController {

  private final CovidCasesStatisticFacade covidCasesStatisticFacade;

  @GetMapping("/new-cases")
  public List<CountryCovidStatsDto> getMaxAndMinNewCasesStatisticByCountryCodes(
      @RequestParam List<String> countryCodes,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate
  ) {
    return covidCasesStatisticFacade.getCovidCasesStatistic(countryCodes, fromDate, toDate);
  }

}
