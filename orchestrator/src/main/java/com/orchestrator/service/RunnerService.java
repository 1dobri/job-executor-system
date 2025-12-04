package com.orchestrator.service;

import com.orchestrator.domain.Runner;
import com.orchestrator.repository.RunnerRepository;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class RunnerService {

    private final RunnerRepository runnerRepo;

    public RunnerService(RunnerRepository runnerRepo) {
        this.runnerRepo = runnerRepo;
    }

    @Transactional
    public Runner registerRunner(String name) {
        return runnerRepo.findByName(name)
                .map(r -> {
                    r.setLastHeartbeat(Instant.now());
                    r.setStatus("ACTIVE");
                    log.info("refresh runner {}", name);
                    return r;
                })
                .orElseGet(() -> {
                    Runner r = new Runner();
                    r.setName(name);
                    r.setLastHeartbeat(Instant.now());
                    r.setStatus("ACTIVE");
                    log.info("Registering new runner", name);
                    return runnerRepo.save(r);
                });
    }

    @Transactional
    public void heartbeat(UUID runnerId) {
        Runner r = runnerRepo.findById(runnerId).orElseThrow();
        r.setLastHeartbeat(Instant.now());
        r.setStatus("ACTIVE");
    }

    public List<Runner> list() {
        return runnerRepo.findAll();
    }
}
