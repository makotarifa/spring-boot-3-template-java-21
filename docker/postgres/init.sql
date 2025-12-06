-- Optional initialization script executed by the official postgres image
-- Creates a sample table used by the application tests
CREATE TABLE IF NOT EXISTS app_test (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL
);
