package com.runner.runner;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.runner.job.JobHandlerRegistry;

import reactor.core.publisher.Mono;

@Component
public class PollingScheduler {

    private final WebClient client;
    private final RunnerState state;
    private final JobHandlerRegistry handlers;

    public PollingScheduler(WebClient client,
                            RunnerState state,
                            JobHandlerRegistry handlers) {
        this.client = client;
        this.state = state;
        this.handlers = handlers;
    }

    @Scheduled(fixedDelay = 2000)
    public void poll() {
        if (!state.isRegistered()) return;

        client.post()
                .uri("/runners/" + state.getRunnerId() + "/next-job")
                .retrieve()
                .bodyToMono(JobExecutionDto.class)
                .flatMap(this::processJob)
                .onErrorResume(e -> Mono.empty())
                .subscribe();
    }

    private Mono<Void> processJob(JobExecutionDto exec) {
        if (exec == null || exec.id() == null) {
            return Mono.empty();
        }

        boolean success = handlers.handle(exec.type(), exec.payload());
        String error = success ? null : "Simulated failure";

        var result = new CompleteRequest(success, error);

        return client.post()
                .uri("/executions/" + exec.id() + "/complete")
                .bodyValue(result)
                .retrieve()
                .toBodilessEntity()
                .then();
    }

    public record JobExecutionDto(java.util.UUID id, String type, String payload) {}
    public record CompleteRequest(boolean success, String errorMessage) {}
}
