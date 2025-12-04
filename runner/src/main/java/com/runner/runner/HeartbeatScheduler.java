package com.runner.runner;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class HeartbeatScheduler {

    private final WebClient client;
    private final RunnerState state;

    public HeartbeatScheduler(WebClient client, RunnerState state) {
        this.client = client;
        this.state = state;
    }

    @Scheduled(fixedDelay = 5000)
    public void heartbeat() {
        if (!state.isRegistered()) return;

        client.post()
                .uri("/runners/" + state.getRunnerId() + "/heartbeat")
                .retrieve()
                .toBodilessEntity()
                .subscribe();
    }
}