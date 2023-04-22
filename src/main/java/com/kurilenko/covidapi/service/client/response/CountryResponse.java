package com.kurilenko.covidapi.service.client.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountryResponse {

  @JsonProperty("Country")
  private String country;

  @JsonProperty("Slug")
  private String slug;

  @JsonProperty("ISO2")
  private String iso2;
}
