CREATE TABLE IF NOT EXISTS tags (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    text VARCHAR(256) NOT NULL
);

CREATE TABLE IF NOT EXISTS posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(256) NOT NULL,
    text VARCHAR(256) NOT NULL,
    likesCount INT NOT NULL,
    commentsCount INT NOT NULL
);

CREATE TABLE IF NOT EXISTS post_tag (
    post_id BIGINT NOT NULL,
    tag_id BIGINT NOT NULL,
    PRIMARY KEY (post_id, tag_id),
    FOREIGN KEY (post_id) REFERENCES posts(id),
    FOREIGN KEY (tag_id) REFERENCES tags(id)
);



INSERT INTO tags (text) VALUES
('java'),
('spring'),
('backend'),
('h2'),
('jdbc');

INSERT INTO posts (title, text, likesCount, commentsCount) VALUES
('Как работает Spring MVC', 'Разбираем основы и архитектуру Spring MVC...', 10, 2),
('Работа с JDBC без ORM', 'Подключение, запросы, RowMapper...', 7, 1),
('Используем H2 для разработки', 'Объясняю, как настроить H2 Database...', 5, 0);

-- Пост 1: java, spring, backend
INSERT INTO post_tag (post_id, tag_id) VALUES
(1, 1),
(1, 2),
(1, 3);

-- Пост 2: java, jdbc
INSERT INTO post_tag (post_id, tag_id) VALUES
(2, 1),
(2, 5);

-- Пост 3: java, h2
INSERT INTO post_tag (post_id, tag_id) VALUES
(3, 1),
(3, 4);


