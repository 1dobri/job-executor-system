# job-executor-system

This is a system is using docker-compose to run. 
You need to have docker installed on your computer.

To start the project. 
  1. Make sure the Docker daemon is running
  2. Then use this command in the top directory <docker compose up --build>
  3. This system can scale dynamically by just using docker to start more Runner apps
    - * docker compose up --build --scale runner=3 (will start more runners)

Here is Postman collection to test the whole project:

  -> Orchestrator API.postman_collection.json (in this directory)

General Explanations

General explanations on the choices that have been made are in this document.

Project Overview
Distributed Job Scheduling and Execution System
Overview

This project implements a distributed job scheduling and execution system consisting of two Spring Boot applications (Orchestrator and Runner) and a PostgreSQL database, all managed through Docker Compose.

The Orchestrator is responsible for job creation, scheduling, dispatching, retry handling, and status tracking.
Runner instances register dynamically, poll for available work, execute jobs, and report results back to the Orchestrator.
The system is designed to be horizontally scalable, fault-tolerant, and safe under concurrent load.

Architecture Summary

The Orchestrator exposes APIs for job management, runner registration, and execution status.

Runners register on startup, send periodic heartbeats, and continuously poll for pending jobs.

Jobs and executions are persisted in PostgreSQL, ensuring durability and recovery after restarts.

Concurrency and duplicate execution prevention are enforced through database row locking.

Retry behavior is implemented with exponential backoff and capped at three attempts.

How Requirements Are Addressed
Accepting and Managing Job Definitions

Jobs are created through the /jobs API. Each job includes a unique ID, type, and payload.
The Orchestrator persists jobs and creates the initial PENDING execution entry.

Triggering Jobs

Jobs are triggered via an HTTP POST request. Scheduled jobs can be supported through the scheduleExpression field and an extended scheduling component.

Executing Jobs in a Distributed Manner

Runners poll /runners/{id}/next-job to fetch work.
The selection of pending jobs uses FOR UPDATE SKIP LOCKED to ensure only one runner acquires a job.

Monitoring Execution Outcomes

Execution records store status, attempts, timestamps, and error messages.
APIs such as /executions/{id} and /executions/active expose execution state for inspection.

Retry and Duplicate Prevention

Failed executions trigger a new execution attempt with exponential backoff.
Attempts are limited to three.
Duplicate execution is prevented by transactional locking, idempotent completion, and stable state transitions.

Functional Requirements
1. Register Job Runners

Runners register via /runners/register and are stored with a unique identifier.
Heartbeats update their status and ensure they remain discoverable.

2. Trigger Jobs

Jobs are triggered via /jobs with type and payload.
The Orchestrator determines dispatch timing based on execution state.

3. Execute Jobs

Runners simulate execution through sleep and random success or failure.
Execution results are submitted via /executions/{id}/complete.

4. Handle Failures and Retries

Retries are automatically created with increasing delays (5s, 10s, 20s).
Duplicate execution is avoided through database locking and transactional updates.

5. Expose APIs

APIs allow querying job, runner, and execution state.
Optional endpoint /executions/active lists currently running executions and their assigned runners.

Non-Functional Requirements

Fault tolerance: State stored in PostgreSQL ensures recovery after service restarts.

Concurrency: Row locks and SKIP LOCKED ensure concurrent runners do not collide.

Persistence: Jobs, runners, executions, and retry state persist across sessions.

Scalability: Additional runners can be added through Docker Compose scaling.

Extensibility: New job types can be added via the JobHandler interface and handler registry.

Running Tests

Tests can be run with:

./gradlew test

Assumptions and Trade-offs

A pull-based model was chosen for simplicity; runners request jobs instead of the Orchestrator pushing them.

Database row locking is used instead of distributed locks; adequate for this scope and simpler to maintain.

Execution scheduling uses a custom retry mechanism rather than an external scheduler.

Only the Orchestrator communicates with the database; runners remain stateless for easier horizontal scaling.

Bonus Features

Endpoint /executions/active to monitor which runner is executing which job.

JobHandler registry to support multiple job types.

Docker Compose setup supports dynamic runner scaling.

A database UI is available and started with Docker Compose as well.
It can be accessed through the browser using:

Username: admin@admin.com

Password: admin

Then add the database server in the configuration as follows:

Right-click Servers → Register → Server

General

Name: jobs-db

Connection

Host: db (this is the Docker service name — docker's internal DNS resolves it)

Port: 5432

Username: jobs

Password: jobs