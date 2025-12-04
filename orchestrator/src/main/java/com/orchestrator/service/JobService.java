package com.orchestrator.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.orchestrator.domain.ExecutionStatus;
import com.orchestrator.domain.Job;
import com.orchestrator.domain.JobExecution;
import com.orchestrator.repository.JobExecutionRepository;
import com.orchestrator.repository.JobRepository;

import java.time.Instant;

@Service
public class JobService {

    private final JobRepository jobRepo;
    private final JobExecutionRepository execRepo;

    public JobService(JobRepository jobRepo, JobExecutionRepository execRepo) {
        this.jobRepo = jobRepo;
        this.execRepo = execRepo;
    }

    @Transactional
    public JobExecution createJob(String type, String payload, String scheduleExpression) {
        Job job = new Job();
        job.setType(type);
        job.setPayload(payload);
        job.setScheduleExpression(scheduleExpression);
        jobRepo.save(job);

        JobExecution exec = new JobExecution();
        exec.setJob(job);
        exec.setStatus(ExecutionStatus.PENDING);
        exec.setAttempt(1);
        exec.setNextRunAt(Instant.now());
        execRepo.save(exec);

        return exec;
    }

    public Job getJob(java.util.UUID id) {
        return jobRepo.findById(id).orElseThrow();
    }
}
