package com.kurilenko.covidapi.service.client.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CovidDataResponse {

  @JsonProperty("Country")
  private String country;

  @JsonProperty("Cases")
  private int cases;

  @JsonProperty("Date")
  private LocalDateTime date;

}
