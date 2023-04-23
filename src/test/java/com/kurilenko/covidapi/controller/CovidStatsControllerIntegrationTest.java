package com.kurilenko.covidapi.controller;

import static com.kurilenko.covidapi.WiremockUtils.stubCovidApiCountryConfirmedStatus;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kurilenko.covidapi.BaseIntegrationTest;
import com.kurilenko.covidapi.dto.CountryDto;
import com.kurilenko.covidapi.service.CountryService;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlConfig;

@TestPropertySource(properties = {"wiremock.reset-mappings-after-each-test=true"})
public class CovidStatsControllerIntegrationTest extends BaseIntegrationTest {

  private static final String STATISTIC_NEW_CASES_PATH = "/api/statistics/new-cases";

  private static final CountryDto DOMINICA_COUNTRY_DTO = CountryDto.builder()
      .slug("dominica")
      .code("DM")
      .build();

  private static final CountryDto ESTONIA_COUNTRY_DTO = CountryDto.builder()
      .slug("estonia")
      .code("EE")
      .build();

  @MockBean
  private CountryService countryService;

  @Test
  @Sql(scripts = "/sql-test-scripts/test-countries.sql",
      executionPhase = ExecutionPhase.BEFORE_TEST_METHOD,
      config = @SqlConfig(encoding = "UTF8"))
  public void getMaxAndMinNewCasesStatisticByCountryCodesTest() throws Exception {
    List<String> countryCodes = Arrays.asList("DM", "EE");
    LocalDate fromDate = LocalDate.of(2022, 4, 1);
    LocalDate toDate = LocalDate.of(2022, 4, 10);

    stubCovidApiCountryConfirmedStatus("dominica", "status/dominica_response.json");
    stubCovidApiCountryConfirmedStatus("estonia", "status/estonia_response.json");

    when(countryService.findByCountryCodes(any()))
        .thenReturn(List.of(DOMINICA_COUNTRY_DTO, ESTONIA_COUNTRY_DTO));

    mockMvc.perform(get(STATISTIC_NEW_CASES_PATH)
            .param("countryCodes", String.join(",", countryCodes))
            .param("fromDate", fromDate.toString())
            .param("toDate", toDate.toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].maxNewCases").value(56))
        .andExpect(jsonPath("$.[0].minNewCases").value(0))
        .andExpect(jsonPath("$.[0].countryCode").value("DM"))
        .andExpect(jsonPath("$.[1].maxNewCases").value(1100))
        .andExpect(jsonPath("$.[1].minNewCases").value(347))
        .andExpect(jsonPath("$.[1].countryCode").value("EE"));
  }

}
