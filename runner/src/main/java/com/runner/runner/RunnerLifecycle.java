package com.runner.runner;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.util.retry.Retry;

import org.springframework.boot.context.event.ApplicationReadyEvent;

@Slf4j
@Component
public class RunnerLifecycle {

    private final WebClient client;
    private final RunnerState state;
    private final String runnerName;

    public RunnerLifecycle(WebClient client,
                           RunnerState state,
                           @Value("${runner.name}") String runnerName) {
        this.client = client;
        this.state = state;
        this.runnerName = runnerName;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void register() {
        client.post()
                .uri("/runners/register?name=" + runnerName)
                .retrieve()
                .bodyToMono(RegisterResponse.class)
                .retryWhen(
                  Retry.backoff(10, Duration.ofSeconds(2)) // try up to 10 times
                  .maxBackoff(Duration.ofSeconds(10))
                )
                .doOnNext(resp -> {
                  state.setRunnerId(resp.id()); 
                  log.info("Runner successfully registered: {}", state.getRunnerId());
                })
                .doOnError(e -> log.error(
                            "Error when calling orchestrator, runnerId={}", 
                            state.getRunnerId(), 
                            e
                ))
                .block();
    }

    public record RegisterResponse(java.util.UUID id, String name) {}
}