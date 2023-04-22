package com.kurilenko.covidapi.config.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "web-client.retry")
public class WebClientRetryProperties {

  private Integer maxAttempts;
  private BackoffProperty minBackoff;

  @Getter
  @Setter
  public static class BackoffProperty {

    private String unit;
    private Long amount;
  }

}
