package com.orchestrator.repository;

import com.orchestrator.domain.JobExecution;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
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

    @Query(value = """
        SELECT e.*
        FROM job_execution e
        INNER JOIN runner r ON e.runner_id = r.id
        INNER JOIN job j ON e.job_id = j.id
        WHERE e.status = 'RUNNING'
        """,
    nativeQuery = true
    )
    List<JobExecution> findActiveExecutions();
}
