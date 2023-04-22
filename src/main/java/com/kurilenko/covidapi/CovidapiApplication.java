package com.kurilenko.covidapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@ConfigurationPropertiesScan("com.kurilenko.covidapi.config")
public class CovidapiApplication {

  public static void main(String[] args) {
    SpringApplication.run(CovidapiApplication.class, args);
  }

}
