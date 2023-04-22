package com.kurilenko.covidapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CountryCovidStatsDto {

  private Integer minNewCases;
  private Integer maxNewCases;
  private String countryCode;

}
