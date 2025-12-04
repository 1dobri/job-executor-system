package com.orchestrator.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
public class JobExecution {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    private Job job;

    @ManyToOne
    private Runner runner;

    @Enumerated(EnumType.STRING)
    private ExecutionStatus status;

    private int attempt;

    private Instant nextRunAt;

    private Instant startedAt;

    private Instant finishedAt;

    @Column(columnDefinition = "TEXT")
    private String errorMessage;

    private Instant createdAt = Instant.now();
}
