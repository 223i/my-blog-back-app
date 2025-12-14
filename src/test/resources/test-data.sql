-- Теги
INSERT INTO tags (text) VALUES
('java'),
('spring'),
('testing');

-- Посты
INSERT INTO posts (title, text, likesCount, commentsCount) VALUES
(
 'Тестирование Spring Boot',
 'Spring Boot упрощает разработку на Spring. В этом посте несколько примеров тестов.',
 5, 2
),
(
 'Работа с H2',
 'H2 — in-memory база для тестов. Отлично подходит для интеграционных тестов.',
 3, 1
);

-- Связь постов и тегов
INSERT INTO post_tag VALUES
(1, 1), (1, 2), (1, 3);
INSERT INTO post_tag VALUES
(2, 1), (2, 3);

-- Комментарии
INSERT INTO comments (text, post_id) VALUES
('Отличная статья для тестов', 1),
('Хороший пример с H2', 2),
('Немного коротко, но понятно', 1);

-- Изображения (одна картинка для всех постов)
INSERT INTO images (post_id) VALUES 1;
