package com.kurilenko.covidapi.domain.repository;

import com.kurilenko.covidapi.domain.entity.CovidCasesEntity;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CovidCasesRepository extends JpaRepository<CovidCasesEntity, Long> {

  Optional<CovidCasesEntity> findByCountry_CodeAndDate(String code, LocalDate date);

  @Query(value = "select distinct country_code from covid_cases", nativeQuery = true)
  List<String> findExistedCountryCodes();

  /*
  if need to find the date of the maximum and minimum cases, we can rewrite sql query
  for example:
  select new_cases, date, country_code from covid_cases where ...
  sort by new_cases [ASC/DESC] limit 1
   */
  @Query(value = """
      select max(cc.new_cases) as maxNewCases,
             min(cc.new_cases) as minNewCases,
             cc.country_code as countryCode
      from covid_cases cc
      where cc.country_code in (:countryCodes)
        and date >= :fromDate
        and date <= :toDate
      group by cc.country_code""", nativeQuery = true)
  List<CountryCovidStatistic> findMaxAndMinCasesByCountryCodes(
      @Param("countryCodes") List<String> countryCodes,
      @Param("fromDate") LocalDate fromDate,
      @Param("toDate") LocalDate toDate);

  interface CountryCovidStatistic {

    Integer getMaxNewCases();

    Integer getMinNewCases();

    String getCountryCode();
  }

}
