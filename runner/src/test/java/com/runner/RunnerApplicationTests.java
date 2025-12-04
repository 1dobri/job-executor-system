package com.runner;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.runner.runner.RunnerLifecycle;

@SpringBootTest
class RunnerApplicationTests {


  @TestConfiguration
  static class TestConfig {
      @Bean
      public RunnerLifecycle runnerLifecycle() {
          return Mockito.mock(RunnerLifecycle.class);
      }
  }

	@Test
	void contextLoads() {
	}
}
