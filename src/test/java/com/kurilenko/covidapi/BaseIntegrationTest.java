package com.kurilenko.covidapi;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.kurilenko.covidapi.config.PostgresInitializer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.server.EnableStubRunnerServer;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@EnableStubRunnerServer
@AutoConfigureMockMvc
@AutoConfigureWireMock(port = 0)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration(
    initializers = {PostgresInitializer.class})
public class BaseIntegrationTest {

  private static WireMockServer wireMockServer;

  @Autowired
  protected MockMvc mockMvc;

  @BeforeAll
  static void init() {
    wireMockServer = new WireMockServer(
        new WireMockConfiguration().port(80)
    );
    wireMockServer.start();
    WireMock.configureFor("localhost", 80);
  }

  @AfterAll
  static void after() {
    wireMockServer.stop();
  }

}
