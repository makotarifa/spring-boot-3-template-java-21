-- Flyway migration file to create an example table
CREATE TABLE IF NOT EXISTS app_test (
    id SERIAL PRIMARY KEY,
    name TEXT NOT NULL
);
