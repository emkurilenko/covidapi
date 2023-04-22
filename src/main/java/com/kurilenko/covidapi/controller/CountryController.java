package com.kurilenko.covidapi.controller;

import com.kurilenko.covidapi.dto.CountryDto;
import com.kurilenko.covidapi.service.CountryService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/country")
@RequiredArgsConstructor
public class CountryController {

  private final CountryService countryService;

  @GetMapping
  public List<CountryDto> availableCountries() {
    return countryService.getAll();
  }

}
