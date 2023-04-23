package com.kurilenko.covidapi.util;

import com.kurilenko.covidapi.domain.entity.BaseEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BatchPersist {

  @PersistenceContext
  private EntityManager entityManager;

  @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
  private int batchSize;

  public <T extends BaseEntity<?>> Collection<T> batchInsert(Collection<T> entities) {
    Optional.ofNullable(entities)
        .orElseThrow(() ->
            new IllegalArgumentException("The given Iterable of entities not be null!"));
    int i = 0;
    List<T> persisted = new ArrayList<>();

    for (T entity : entities) {
      entityManager.persist(entity);
      persisted.add(entity);
      i++;
      // Flush a batch of inserts and release memory
      if (i % batchSize == 0 && i > 0) {
        entityManager.flush();
        entityManager.clear();
        i = 0;
      }
    }
    if (i > 0) {
      entityManager.flush();
      entityManager.clear();
    }
    return persisted;
  }

}