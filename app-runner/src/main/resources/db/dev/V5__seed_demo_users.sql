INSERT INTO users (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)
VALUES ('user', '$2a$10$CwTycUXWue0Thq9StjUM0uJ8f7GZpQJf4qQ9jG7aWZrQWQnQG5F.S', true, true, true, true)
ON CONFLICT (username) DO NOTHING;

INSERT INTO authorities (username, authority)
VALUES ('user', 'ROLE_USER')
ON CONFLICT DO NOTHING;
