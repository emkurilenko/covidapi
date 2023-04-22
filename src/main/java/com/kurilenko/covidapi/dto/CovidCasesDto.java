package com.kurilenko.covidapi.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CovidCasesDto {

  private Integer cases;
  private LocalDate date;
  private String countryCode;

}
