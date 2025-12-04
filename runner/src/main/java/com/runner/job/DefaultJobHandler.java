package com.runner.job;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import java.util.Random;

@Slf4j
@Component("DEFAULT")
public class DefaultJobHandler implements JobHandler {

    private final String DELAY_IN_MILLIS = "10000";
    private final Random random = new Random();

    @Override
    public boolean execute(String payload) {
        try {
            log.info("Starting to work on {}", payload);
            Thread.sleep(Long.parseLong(DELAY_IN_MILLIS) + random.nextInt(500));
        } catch (InterruptedException ignored) { 
          log.error("JobHandle count not execute job");
        }
        return random.nextBoolean();
    }
}
