CREATE SCHEMA IF NOT EXISTS management;

CREATE TYPE management.PRIORITY_TYPE AS ENUM
    (
        'HIGH',
        'MEDIUM',
        'LOW'
        );

CREATE TYPE management.STATUS_TYPE AS ENUM
    (
        'CREATED',
        'WAITING',
        'IN_PROGRESS',
        'DONE',
        'CANCELED'
        );

CREATE TABLE management._user
(
    id          UUID PRIMARY KEY,
    keycloak_id UUID         NOT NULL,
    username    VARCHAR(255) NOT NULL,
    email       VARCHAR(255) NOT NULL UNIQUE,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE management.task
(
    id          UUID PRIMARY KEY,
    title       VARCHAR(255)             NOT NULL,
    description TEXT,
    status      management.STATUS_TYPE   NOT NULL,
    priority    management.PRIORITY_TYPE NOT NULL,
    author_id   UUID                     NOT NULL,
    assignee_id UUID,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES management._user (id) ON DELETE CASCADE,
    FOREIGN KEY (assignee_id) REFERENCES management._user (id) ON DELETE SET NULL
);

CREATE TABLE management.comment
(
    id         UUID PRIMARY KEY,
    task_id    UUID NOT NULL,
    author_id  UUID NOT NULL,
    content    TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES management.task (id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES management._user (id) ON DELETE CASCADE
);