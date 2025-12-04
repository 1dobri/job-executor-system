package com.runner.runner;

import java.util.concurrent.Executor;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.runner.job.JobHandlerRegistry;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class PollingScheduler {

    private final WebClient client;
    private final RunnerState state;
    private final JobHandlerRegistry handlers;
    private final Executor jobExecutor;

    public PollingScheduler(WebClient client,
                            RunnerState state,
                            JobHandlerRegistry handlers,
                            Executor jobExecutor) {
        this.client = client;
        this.state = state;
        this.handlers = handlers;
        this.jobExecutor = jobExecutor;
    }

    @Scheduled(fixedDelay = 100)
    public void poll() {
        if (!state.isRegistered()) return;

        log.info("Requesting a job, runner: {}", state.getRunnerId());
        
        client.post()
                .uri("/runners/" + state.getRunnerId() + "/next-job")
                .retrieve()
                .bodyToMono(JobExecutionDto.class)
                .filter(exec -> exec.id() != null)
                .doOnNext(exec ->
                        jobExecutor.execute(() -> executeJob(exec))
                )
                .onErrorResume(e -> Mono.empty())
                .subscribe();
    }

    private void executeJob(JobExecutionDto exec) {
        boolean success = handlers.handle(exec.type(), exec.payload());
        String error = success ? null : "Simulated failure";

        var result = new CompleteRequest(success, error);

        client.post()
                .uri("/executions/" + exec.id() + "/complete")
                .bodyValue(result)
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public record JobExecutionDto(java.util.UUID id, String type, String payload) {}
    public record CompleteRequest(boolean success, String errorMessage) {}
}
