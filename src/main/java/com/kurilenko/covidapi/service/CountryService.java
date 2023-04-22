package com.kurilenko.covidapi.service;

import com.kurilenko.covidapi.domain.entity.CountryEntity;
import com.kurilenko.covidapi.domain.repository.CountryRepository;
import com.kurilenko.covidapi.dto.CountryDto;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CountryService {

  private final CountryRepository countryRepository;

  public List<CountryDto> getAll() {
    return countryRepository.findAll()
        .stream()
        .map(CountryService::mapToDto)
        .toList();
  }

  public List<CountryDto> findByCountryCodes(List<String> countryCodes) {
    return countryRepository.findAllByCodeIn(countryCodes)
        .stream()
        .map(CountryService::mapToDto)
        .toList();
  }

  @Transactional
  public CountryDto save(CountryDto dto) {
    CountryEntity entityForPersist = CountryEntity.builder()
        .slug(dto.getSlug())
        .name(dto.getName())
        .code(dto.getCode())
        .build();
    entityForPersist = countryRepository.save(entityForPersist);
    return mapToDto(entityForPersist);
  }

  @Transactional
  public Optional<CountryDto> getByCode(String code) {
    return countryRepository.findCountryEntityByCode(code)
        .map(CountryService::mapToDto);
  }

  private static CountryDto mapToDto(CountryEntity entity) {
    return CountryDto.builder()
        .name(entity.getName())
        .slug(entity.getSlug())
        .code(entity.getCode())
        .build();
  }

}
