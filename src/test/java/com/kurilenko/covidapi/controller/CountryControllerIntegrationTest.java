package com.kurilenko.covidapi.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kurilenko.covidapi.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.jdbc.SqlConfig;

public class CountryControllerIntegrationTest extends BaseIntegrationTest {

  public static final String COUNTRY_PATH = "/api/country";

//  @Test
  @Sql(scripts = "/sql-test-scripts/test-countries.sql",
      executionPhase = ExecutionPhase.BEFORE_TEST_METHOD,
      config = @SqlConfig(encoding = "UTF8"))
  public void getAvailableCountries() throws Exception {

    mockMvc.perform(get(COUNTRY_PATH)
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

}
