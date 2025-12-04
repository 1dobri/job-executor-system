package com.orchestrator.repository;

import com.orchestrator.domain.JobExecution;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface JobExecutionRepository extends JpaRepository<JobExecution, UUID> {

    @Query(value = """
        SELECT * FROM job_execution
        WHERE status = 'PENDING'
        AND next_run_at <= :now
        ORDER BY next_run_at
        LIMIT 1
        FOR UPDATE SKIP LOCKED
        """,
        nativeQuery = true)
    Optional<JobExecution> findNextPendingExecutionForUpdate(@Param("now") Instant now);
}
