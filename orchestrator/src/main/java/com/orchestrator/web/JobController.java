package com.orchestrator.web;

import org.springframework.web.bind.annotation.*;

import com.orchestrator.domain.Job;
import com.orchestrator.domain.JobExecution;
import com.orchestrator.service.JobService;

import java.util.UUID;

@RestController
@RequestMapping("/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public JobExecution create(@RequestBody JobRequest req) {
        return jobService.createJob(req.type(), req.payload(), req.scheduleExpression());
    }

    @GetMapping("/{id}")
    public Job get(@PathVariable UUID id) {
        return jobService.getJob(id);
    }

    public record JobRequest(String type, String payload, String scheduleExpression) {}
}
