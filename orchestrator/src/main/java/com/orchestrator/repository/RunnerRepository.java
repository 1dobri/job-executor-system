package com.orchestrator.repository;

import com.orchestrator.domain.Runner;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RunnerRepository extends JpaRepository<Runner, UUID> {
    Optional<Runner> findByName(String name);
}
