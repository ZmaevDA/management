CREATE TABLE users (
                       id SERIAL PRIMARY KEY, -- Уникальный идентификатор пользователя
                       username VARCHAR(50) NOT NULL, -- Имя пользователя
                       email VARCHAR(100) NOT NULL UNIQUE, -- Email пользователя
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- Дата создания пользователя
);

CREATE TABLE tasks (
                       id SERIAL PRIMARY KEY, -- Уникальный идентификатор задачи
                       title VARCHAR(255) NOT NULL, -- Заголовок задачи
                       description TEXT, -- Описание задачи
                       status VARCHAR(50) NOT NULL, -- Статус задачи ("в ожидании", "в процессе", "завершено")
                       priority VARCHAR(50) NOT NULL, -- Приоритет задачи ("высокий", "средний", "низкий")
                       author_id INT NOT NULL, -- ID автора задачи
                       assignee_id INT, -- ID исполнителя задачи
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Дата создания задачи
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Дата последнего обновления задачи
                       FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE CASCADE,
                       FOREIGN KEY (assignee_id) REFERENCES users (id) ON DELETE SET NULL
);

CREATE TABLE comments (
                          id SERIAL PRIMARY KEY, -- Уникальный идентификатор комментария
                          task_id INT NOT NULL, -- ID задачи
                          user_id INT NOT NULL, -- ID пользователя, оставившего комментарий
                          content TEXT NOT NULL, -- Текст комментария
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Дата создания комментария
                          FOREIGN KEY (task_id) REFERENCES tasks (id) ON DELETE CASCADE,
                          FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);