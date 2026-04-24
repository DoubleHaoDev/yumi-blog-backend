-- Create test table
CREATE TABLE IF NOT EXISTS test (
    id SERIAL PRIMARY KEY,
    message VARCHAR(255) NOT NULL
);

-- Insert a test entry
INSERT INTO test (message) VALUES ('Hello from yumi-blog!');

