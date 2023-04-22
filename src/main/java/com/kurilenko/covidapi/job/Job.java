package com.kurilenko.covidapi.job;

public interface Job {

  void schedule();

  boolean toExecute();

  String type();

}
