package com.kurilenko.covidapi.job;

import lombok.extern.slf4j.Slf4j;

/*
If we want to use few instances application, we need use distributed job,
but for simple implementation we can use shedlock library
urls: https://www.baeldung.com/shedlock-spring
https://github.com/lukas-krecan/ShedLock
*/
@Slf4j
public abstract class BaseJob implements Job {

  @Override
  public void schedule() {
    log.trace("Starting task: {}", type());
    try {
      toExecute();
    } catch (Exception e) {
      log.error("Failed execute task: {}, error: {}", type(), e.getMessage());
    }
  }

  @Override
  public String type() {
    return this.getClass().getSimpleName();
  }
}
