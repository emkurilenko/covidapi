package com.kurilenko.covidapi.domain.repository;

import com.kurilenko.covidapi.domain.entity.CountryEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository extends JpaRepository<CountryEntity, Long> {

  Optional<CountryEntity> findCountryEntityByCode(String code);

  List<CountryEntity> findAllByCodeIn(List<String> codes);

  boolean existsByCode(String code);

}
