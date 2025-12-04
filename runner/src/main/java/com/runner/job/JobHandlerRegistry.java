package com.runner.job;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JobHandlerRegistry {

    private final Map<String, JobHandler> handlers;

    public JobHandlerRegistry(Map<String, JobHandler> handlers) {
        this.handlers = handlers;
    }

    public boolean handle(String type, String payload) {
        JobHandler handler = handlers.getOrDefault(type, handlers.get("DEFAULT"));
        return handler.execute(payload);
    }
}
