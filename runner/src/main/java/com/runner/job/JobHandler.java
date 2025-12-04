package com.runner.job;

public interface JobHandler {
    boolean execute(String payload);
}
