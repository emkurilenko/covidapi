package com.kurilenko.covidapi.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "covid_cases")
public class CovidCasesEntity implements BaseEntity<Long> {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqGen")
  @SequenceGenerator(name = "seqGen", sequenceName = "covid_cases_id_seq")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "country_code")
  private CountryEntity country;

  private LocalDate date;

  private Integer newCases;

}
