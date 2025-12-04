package com.runner.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.runner.runner.RunnerState;

@Configuration
public class StateConfig {

    @Bean
    public RunnerState runnerState() {
        return new RunnerState();
    }
}
