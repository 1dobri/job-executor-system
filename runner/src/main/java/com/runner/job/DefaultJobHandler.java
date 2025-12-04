package com.runner.job;

import org.springframework.stereotype.Component;

import java.util.Random;

@Component("DEFAULT")
public class DefaultJobHandler implements JobHandler {

    private final Random random = new Random();

    @Override
    public boolean execute(String payload) {
        try {
            Thread.sleep(1000 + random.nextInt(1000));
        } catch (InterruptedException ignored) { }
        return random.nextBoolean();
    }
}
