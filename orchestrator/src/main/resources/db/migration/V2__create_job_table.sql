CREATE TABLE job (
    id UUID PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    payload TEXT,
    schedule_expression VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);