package com.kurilenko.covidapi.service;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.kurilenko.covidapi.domain.entity.CountryEntity;
import com.kurilenko.covidapi.domain.entity.CovidCasesEntity;
import com.kurilenko.covidapi.domain.repository.CovidCasesRepository;
import com.kurilenko.covidapi.domain.repository.CovidCasesRepository.CountryCovidStatistic;
import com.kurilenko.covidapi.dto.CountryCovidStatsDto;
import com.kurilenko.covidapi.dto.CovidCasesDto;
import com.kurilenko.covidapi.util.BatchPersistService;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(classes = CovidCasesService.class)
public class CovidCasesServiceTest {

  private static final LocalDate DATE = LocalDate.of(2020, 10, 15);
  private static final LocalDate FROM_DATE = LocalDate.of(2020, 10, 10);
  private static final LocalDate TO_DATE = LocalDate.of(2020, 11, 10);
  private static final String COUNTRY_CODE = "SL";
  private static final CovidCasesDto COVID_CASES_DTO = CovidCasesDto.builder()
      .cases(10)
      .countryCode(COUNTRY_CODE)
      .date(DATE)
      .build();
  private static final CovidCasesEntity COVID_CASES_ENTITY = CovidCasesEntity.builder()
      .newCases(10)
      .country(new CountryEntity(COUNTRY_CODE))
      .date(DATE)
      .build();
  private static final CountryCovidStatsDto COUNTRY_COVID_STATS = CountryCovidStatsDto.builder()
      .countryCode(COUNTRY_CODE)
      .maxNewCases(10)
      .minNewCases(4)
      .build();

  private static final CountryCovidStatistic COUNTRY_COVID_STATISTIC = new CountryCovidStatistic() {
    @Override
    public Integer getMaxNewCases() {
      return COUNTRY_COVID_STATS.getMaxNewCases();
    }

    @Override
    public Integer getMinNewCases() {
      return COUNTRY_COVID_STATS.getMinNewCases();
    }

    @Override
    public String getCountryCode() {
      return COUNTRY_COVID_STATS.getCountryCode();
    }
  };


  @MockBean
  private CovidCasesRepository covidCasesRepository;
  @MockBean
  private BatchPersistService batchPersist;

  @Autowired
  private CovidCasesService covidCasesService;

  @Test
  public void findExistedCountryCodesReturnEmptyListTest() {
    when(covidCasesRepository.findExistedCountryCodes())
        .thenReturn(of());

    List<String> existedCountryCodes = covidCasesService.findExistedCountryCodes();
    Assertions.assertTrue(existedCountryCodes.isEmpty());
  }

  @Test
  public void findExistedCountryCodesReturnOneElementTest() {
    when(covidCasesRepository.findExistedCountryCodes())
        .thenReturn(of(COUNTRY_CODE));

    List<String> existedCountryCodes = covidCasesService.findExistedCountryCodes();
    assertFalse(existedCountryCodes.isEmpty());
    assertEquals(COUNTRY_CODE, existedCountryCodes.get(0));
  }

  @Test
  public void findNotExistedCountryCodesTest() {
    when(covidCasesRepository.findExistedCountryCodes())
        .thenReturn(of());

    List<String> notExistedCountryCodes =
        covidCasesService.findNotExistedCountryCodes(of(COUNTRY_CODE));
    assertFalse(notExistedCountryCodes.isEmpty());
    assertEquals(COUNTRY_CODE, notExistedCountryCodes.get(0));
  }

  @Test
  public void getNewCasesStatisticByCountryCodesReturnEmptyResultTest() {
    when(covidCasesRepository.findMaxAndMinCasesByCountryCodes(any(), any(), any()))
        .thenReturn(of());

    List<CountryCovidStatsDto> result =
        covidCasesService.getNewCasesStatisticByCountryCodes(of(COUNTRY_CODE), FROM_DATE, TO_DATE);

    assertTrue(result.isEmpty());
  }

  @Test
  public void getNewCasesStatisticByCountryCodesSuccessfullyTest() {
    when(covidCasesRepository.findMaxAndMinCasesByCountryCodes(any(), any(), any()))
        .thenReturn(of(COUNTRY_COVID_STATISTIC));

    List<CountryCovidStatsDto> result =
        covidCasesService.getNewCasesStatisticByCountryCodes(of(COUNTRY_CODE), FROM_DATE, TO_DATE);

    assertFalse(result.isEmpty());
    assertEquals(COUNTRY_COVID_STATS, result.get(0));
  }

  @Test
  public void saveUsingBatchTest() {
    when(batchPersist.batchInsert(any()))
        .thenReturn(of(COVID_CASES_ENTITY));

    List<CovidCasesDto> result = covidCasesService.saveUsingBatch(of(COVID_CASES_DTO));
    assertFalse(result.isEmpty());
    assertEquals(COVID_CASES_DTO, result.get(0));
  }

  @Test
  public void updateNewCasesByDateRecordExistedInDBTest() {
    when(covidCasesRepository.findByCountry_CodeAndDate(any(), any()))
        .thenReturn(Optional.of(COVID_CASES_ENTITY));

    when(covidCasesRepository.save(any()))
        .thenReturn(COVID_CASES_ENTITY);

    CovidCasesDto persisted = covidCasesService.updateNewCasesByDate(COVID_CASES_DTO);
    assertEquals(COVID_CASES_DTO, persisted);
    verify(covidCasesRepository)
        .findByCountry_CodeAndDate(COVID_CASES_DTO.getCountryCode(), COVID_CASES_DTO.getDate());
  }

}
