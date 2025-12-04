package com.orchestrator.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Job {

    @Id
    @GeneratedValue
    private UUID id;

    private String type;

    @Column(columnDefinition = "TEXT")
    private String payload;

    private String scheduleExpression;

    private Instant createdAt = Instant.now();
}

