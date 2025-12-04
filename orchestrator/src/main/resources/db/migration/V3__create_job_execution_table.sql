CREATE TABLE job_execution (
    id UUID PRIMARY KEY,

    -- foreign keys
    job_id UUID NOT NULL,
    runner_id UUID,

    status VARCHAR(50) NOT NULL,
    attempt INT NOT NULL,

    next_run_at TIMESTAMP,
    started_at TIMESTAMP,
    finished_at TIMESTAMP,

    error_message TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_job_execution_job
        FOREIGN KEY (job_id) REFERENCES job(id),

    CONSTRAINT fk_job_execution_runner
        FOREIGN KEY (runner_id) REFERENCES runner(id)
);