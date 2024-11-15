CREATE TABLE _user
(
    id         UUID PRIMARY KEY,
    username   VARCHAR(50)  NOT NULL,
    email      VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE task
(
    id          UUID PRIMARY KEY,
    title       VARCHAR(255) NOT NULL,
    description TEXT,
    status      VARCHAR(50)  NOT NULL,
    priority    VARCHAR(50)  NOT NULL,
    author_id   UUID         NOT NULL,
    assignee_id UUID,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES _user (id) ON DELETE CASCADE,
    FOREIGN KEY (assignee_id) REFERENCES _user (id) ON DELETE SET NULL
);

CREATE TABLE comment
(
    id         UUID PRIMARY KEY,
    task_id    UUID NOT NULL,
    author_id    UUID NOT NULL,
    content    TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (task_id) REFERENCES task (id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES _user (id) ON DELETE CASCADE
);