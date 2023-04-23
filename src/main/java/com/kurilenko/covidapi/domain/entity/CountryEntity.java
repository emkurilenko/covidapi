package com.kurilenko.covidapi.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "countries")
public class CountryEntity implements BaseEntity<String> {

  @Id
  private String code;

  private String name;

  private String slug;

  public CountryEntity(String code) {
    this.code = code;
  }

  @Override
  public String getId() {
    return code;
  }
}
