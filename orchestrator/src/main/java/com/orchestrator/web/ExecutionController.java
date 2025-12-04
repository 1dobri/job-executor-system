package com.orchestrator.web;

import org.springframework.web.bind.annotation.*;

import com.orchestrator.domain.JobExecution;
import com.orchestrator.service.ExecutionService;

import java.util.UUID;

@RestController
@RequestMapping("/executions")
public class ExecutionController {

    private final ExecutionService executionService;

    public ExecutionController(ExecutionService executionService) {
        this.executionService = executionService;
    }

    @GetMapping("/{id}")
    public JobExecution get(@PathVariable UUID id) {
        return executionService.getExecution(id);
    }

    @PostMapping("/{id}/complete")
    public void complete(@PathVariable UUID id, @RequestBody CompleteRequest req) {
        executionService.completeExecution(id, req.success(), req.errorMessage());
    }

    public record CompleteRequest(boolean success, String errorMessage) {}
}
