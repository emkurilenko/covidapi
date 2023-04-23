package com.kurilenko.covidapi.service;


import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kurilenko.covidapi.domain.entity.CountryEntity;
import com.kurilenko.covidapi.domain.repository.CountryRepository;
import com.kurilenko.covidapi.dto.CountryDto;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = CountryService.class)
public class CountryServiceTest {

  public static final String COUNTRY_CODE = "SL";
  public static final String PL_COUNTRY_CODE = "PL";
  private static final CountryEntity COUNTRY_ENTITY = CountryEntity.builder()
      .slug("slug")
      .name("name")
      .code(COUNTRY_CODE)
      .build();

  private static final CountryDto COUNTRY_DTO = CountryDto.builder()
      .slug("slug")
      .name("name")
      .code(COUNTRY_CODE)
      .build();

  @MockBean
  private CountryRepository countryRepository;

  @Autowired
  private CountryService countryService;

  @Test
  public void getAllTest() {
    when(countryRepository.findAll())
        .thenReturn(List.of(COUNTRY_ENTITY));

    List<CountryDto> all = countryService.getAll();

    Assertions.assertEquals(1, all.size());
    Assertions.assertEquals(COUNTRY_DTO, all.get(0));
  }

  @Test
  public void findByCountryCodesExistedInDBTest() {
    List<String> countryCodes = List.of(COUNTRY_CODE);

    when(countryRepository.findAllByCodeIn(countryCodes))
        .thenReturn(List.of(COUNTRY_ENTITY));

    List<CountryDto> countries = countryService.findByCountryCodes(countryCodes);
    Assertions.assertEquals(1, countries.size());
    Assertions.assertEquals(COUNTRY_DTO, countries.get(0));
  }

  @Test
  public void findByCountryCodesNotExistedInDBTest() {
    List<String> countryCodes = List.of(PL_COUNTRY_CODE);

    when(countryRepository.findAllByCodeIn(countryCodes))
        .thenReturn(List.of());

    List<CountryDto> countries = countryService.findByCountryCodes(countryCodes);
    Assertions.assertTrue(countries.isEmpty());
  }

  @Test
  public void saveCountryTest() {
    when(countryRepository.save(COUNTRY_ENTITY))
        .thenReturn(COUNTRY_ENTITY);

    CountryDto saved = countryService.save(COUNTRY_DTO);
    verify(countryRepository).save(COUNTRY_ENTITY);
    Assertions.assertEquals(COUNTRY_DTO, saved);
  }

}
