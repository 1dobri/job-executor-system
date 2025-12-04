package com.orchestrator.web;

import com.orchestrator.domain.JobExecution;
import com.orchestrator.domain.Runner;
import com.orchestrator.service.ExecutionService;
import com.orchestrator.service.RunnerService;

import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/runners")
public class RunnerController {

    private final RunnerService runnerService;
    private final ExecutionService executionService;

    public RunnerController(RunnerService runnerService, ExecutionService executionService) {
        this.runnerService = runnerService;
        this.executionService = executionService;
    }

    @PostMapping("/register")
    public Runner register(@RequestParam String name) {
        return runnerService.registerRunner(name);
    }

    @PostMapping("/{id}/heartbeat")
    public void heartbeat(@PathVariable UUID id) {
        runnerService.heartbeat(id);
    }

    @GetMapping
    public List<Runner> list() {
        return runnerService.list();
    }

    @PostMapping("/{id}/next-job")
    public JobExecution nextJob(@PathVariable UUID id) {
        return executionService.fetchNextJobForRunner(id).orElse(null);
    }
}
