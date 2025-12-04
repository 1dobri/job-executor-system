package com.orchestrator.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Entity
@Getter
@Setter
public class Runner {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(unique = true)
    private String name;

    private Instant lastHeartbeat;

    private String status;

    private Instant createdAt = Instant.now();
}
