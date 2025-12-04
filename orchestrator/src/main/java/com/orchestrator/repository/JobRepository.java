package com.orchestrator.repository;

import com.orchestrator.domain.Job;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JobRepository extends JpaRepository<Job, UUID> {
}
