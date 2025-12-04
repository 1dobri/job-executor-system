package com.orchestrator.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orchestrator.domain.ExecutionStatus;
import com.orchestrator.domain.JobExecution;
import com.orchestrator.repository.JobExecutionRepository;
import com.orchestrator.repository.JobRepository;
import com.orchestrator.repository.RunnerRepository;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class ExecutionService {

    private final JobExecutionRepository execRepo;
    private final JobRepository jobRepo;
    private final RunnerRepository runnerRepo;

    public ExecutionService(JobExecutionRepository execRepo,
                            JobRepository jobRepo,
                            RunnerRepository runnerRepo) {
        this.execRepo = execRepo;
        this.jobRepo = jobRepo;
        this.runnerRepo = runnerRepo;
    }

    @Transactional
    public Optional<JobExecution> fetchNextJobForRunner(UUID runnerId) {
        Optional<JobExecution> opt = execRepo.findNextPendingExecutionForUpdate(Instant.now());
        if (opt.isEmpty()) {
            return Optional.empty();
        }

        JobExecution exec = opt.get();
        exec.setRunner(runnerRepo.findById(runnerId).orElseThrow());
        exec.setStatus(ExecutionStatus.RUNNING);
        exec.setStartedAt(Instant.now());
        return Optional.of(exec);
    }

    @Transactional
    public void completeExecution(UUID executionId, boolean success, String error) {
        JobExecution exec = execRepo.findById(executionId).orElseThrow();

        if (exec.getStatus() != ExecutionStatus.RUNNING) {
            return;
        }

        exec.setFinishedAt(Instant.now());

        if (success) {
            exec.setStatus(ExecutionStatus.SUCCESS);
            return;
        }

        exec.setStatus(ExecutionStatus.FAILED);
        exec.setErrorMessage(error);

        int attempt = exec.getAttempt();
        if (attempt >= 3) {
            exec.setStatus(ExecutionStatus.DROPPED);
            return;
        }

        JobExecution retry = new JobExecution();
        retry.setJob(exec.getJob());
        retry.setAttempt(attempt + 1);
        retry.setStatus(ExecutionStatus.PENDING);
        retry.setNextRunAt(Instant.now().plus(backoff(attempt)));

        execRepo.save(retry);
    }

    private Duration backoff(int attempt) {
        long base = 5;
        return Duration.ofSeconds(base * (1L << (attempt - 1)));
    }

    public JobExecution getExecution(UUID id) {
        return execRepo.findById(id).orElseThrow();
    }
}
