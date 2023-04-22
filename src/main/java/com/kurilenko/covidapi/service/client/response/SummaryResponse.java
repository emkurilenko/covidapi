
package com.kurilenko.covidapi.service.client.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class SummaryResponse {

  @JsonProperty("Date")
  private LocalDate date;
  @JsonProperty("Countries")
  private List<CountrySummaryDTO> countries;

  @Data
  public static class CountrySummaryDTO {

    @JsonProperty("Country")
    private String name;
    @JsonProperty("CountryCode")
    private String countryCode;
    @JsonProperty("Slug")
    private String slug;
    @JsonProperty("NewConfirmed")
    private int newConfirmed;
    @JsonProperty("TotalConfirmed")
    private long totalConfirmed;
  }
}