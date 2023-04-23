package com.kurilenko.covidapi.service.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.kurilenko.covidapi.dto.CovidCasesDto;
import com.kurilenko.covidapi.service.CountryService;
import com.kurilenko.covidapi.service.CovidCasesService;
import com.kurilenko.covidapi.service.client.CovidApiClient;
import com.kurilenko.covidapi.service.client.response.CovidDataResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = CovidCasesStatisticFacade.class)
public class CovidCasesStatisticFacadeTest {

  @MockBean
  private CovidApiClient covidApiClient;
  @MockBean
  private CovidCasesService covidCasesService;
  @MockBean
  private CountryService countryService;
  @Autowired
  private CovidCasesStatisticFacade covidCasesStatisticFacade;

  @Test
  public void calculateNewCasesByCountryTest() {
    CovidCasesDto expectedFirstElement = new CovidCasesDto(2, LocalDate.of(2020, 2, 25), "PK");
    CovidCasesDto expectedSecondElement = new CovidCasesDto(0, LocalDate.of(2020, 2, 26), "PK");
    CovidCasesDto expectedThirdElement = new CovidCasesDto(2, LocalDate.of(2020, 2, 27), "PK");
    when(covidApiClient.getCovidDataByCountry(any()))
        .thenReturn(List.of(
            new CovidDataResponse("Pak", 2,
                ZonedDateTime.parse("2020-02-25T00:00:00Z").toLocalDateTime()),
            new CovidDataResponse("Pak", 2,
                ZonedDateTime.parse("2020-02-26T00:00:00Z").toLocalDateTime()),
            new CovidDataResponse("Pak", 4,
                ZonedDateTime.parse("2020-02-27T00:00:00Z").toLocalDateTime())
        ));
    List<CovidCasesDto> newCasesByCountry = covidCasesStatisticFacade.calculateNewCasesByCountry(
        "pac", "PK");

    assertFalse(newCasesByCountry.isEmpty());
    assertEquals(expectedFirstElement, newCasesByCountry.get(0) );
    assertEquals(expectedSecondElement, newCasesByCountry.get(1));
    assertEquals(expectedThirdElement, newCasesByCountry.get(2));
  }

}
