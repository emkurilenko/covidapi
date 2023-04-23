package com.kurilenko.covidapi.service;

import com.kurilenko.covidapi.domain.entity.CountryEntity;
import com.kurilenko.covidapi.domain.entity.CovidCasesEntity;
import com.kurilenko.covidapi.domain.repository.CovidCasesRepository;
import com.kurilenko.covidapi.dto.CountryCovidStatsDto;
import com.kurilenko.covidapi.dto.CovidCasesDto;
import com.kurilenko.covidapi.util.BatchPersistService;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CovidCasesService {

  private final CovidCasesRepository covidCasesRepository;
  private final BatchPersistService batchPersistService;

  @Transactional
  public List<CovidCasesDto> saveUsingBatch(List<CovidCasesDto> dtos) {
    List<CovidCasesEntity> entities = dtos.stream()
        .map(dto -> CovidCasesEntity.builder()
            .newCases(dto.getCases())
            .country(new CountryEntity(dto.getCountryCode()))
            .date(dto.getDate())
            .build())
        .toList();
    return batchPersistService.batchInsert(entities)
        .stream()
        .map(this::mapToDto)
        .toList();
  }

  @Transactional
  public CovidCasesDto updateNewCasesByDate(CovidCasesDto dto) {
    CovidCasesEntity entity = covidCasesRepository.findByCountry_CodeAndDate(
            dto.getCountryCode(), dto.getDate())
        .map(record -> {
          record.setNewCases(dto.getCases());
          return record;
        })
        .orElseGet(() -> CovidCasesEntity.builder()
            .newCases(dto.getCases())
            .country(new CountryEntity(dto.getCountryCode()))
            .date(dto.getDate())
            .build());
    entity = covidCasesRepository.save(entity);
    return mapToDto(entity);
  }

  @Transactional
  public List<CountryCovidStatsDto> getNewCasesStatisticByCountryCodes(
      List<String> countryCodes,
      LocalDate fromDate,
      LocalDate toDate
  ) {
    return covidCasesRepository.findMaxAndMinCasesByCountryCodes(countryCodes, fromDate, toDate)
        .stream()
        .map(result -> CountryCovidStatsDto.builder()
            .maxNewCases(result.getMaxNewCases())
            .minNewCases(result.getMinNewCases())
            .countryCode(result.getCountryCode())
            .build())
        .toList();
  }

  public List<String> findNotExistedCountryCodes(Collection<String> countryCodes) {
    List<String> existedCountryCodes = findExistedCountryCodes();
    return List.copyOf(CollectionUtils.subtract(countryCodes, existedCountryCodes));
  }

  public List<String> findExistedCountryCodes() {
    return covidCasesRepository.findExistedCountryCodes();
  }

  private CovidCasesDto mapToDto(CovidCasesEntity casesEntity) {
    return CovidCasesDto.builder()
        .cases(casesEntity.getNewCases())
        .countryCode(casesEntity.getCountry().getCode())
        .date(casesEntity.getDate())
        .build();
  }

}
