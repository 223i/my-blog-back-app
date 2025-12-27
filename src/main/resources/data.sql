INSERT INTO tags (text) VALUES
('java'),
('spring'),
('backend'),
('h2'),
('jdbc'),
('rest'),
('testing'),
('architecture'),
('database'),
('security');

INSERT INTO posts (title, text, likesCount, commentsCount) VALUES
(
 'Основы Spring Boot',
 '# Spring Boot' || CHAR(10) ||
 '**Spring Boot** упрощает разработку на Spring, предоставляя автоконфигурацию и встроенный сервер.' || CHAR(10) ||
 'Основные модули:' || CHAR(10) ||
 '- Spring MVC' || CHAR(10) ||
 '- Spring Data' || CHAR(10) ||
 '- Spring Security',
 15, 3
),
(
 'Работа с JDBC',
 '# JDBC без ORM' || CHAR(10) ||
 'JDBC позволяет напрямую работать с базой данных, управлять соединениями и выполнять SQL-запросы.' || CHAR(10) ||
 'Советы:' || CHAR(10) ||
 '- Использовать try-with-resources' || CHAR(10) ||
 '- Применять PreparedStatement',
 8, 2
),
(
 'H2 для разработки',
 '# H2 Database' || CHAR(10) ||
 'H2 — лёгкая in-memory база, которая идеально подходит для тестирования и разработки приложений на Java.' || CHAR(10) ||
 'Особенности:' || CHAR(10) ||
 '- Быстрая и лёгкая' || CHAR(10) ||
 '- Поддержка SQL',
 10, 1
),
(
 'Контейнеризация с Docker',
 '# Docker' || CHAR(10) ||
 'Docker позволяет упаковывать приложения с зависимостями в контейнеры, обеспечивая переносимость.' || CHAR(10) ||
 'Основные команды:' || CHAR(10) ||
 '- docker build' || CHAR(10) ||
 '- docker run',
 12, 4
),
(
 'Оркестрация Kubernetes',
 '# Kubernetes' || CHAR(10) ||
 'Kubernetes помогает управлять контейнерами в продакшене, обеспечивая масштабирование и надёжность.' || CHAR(10) ||
 'Основные объекты:' || CHAR(10) ||
 '- Pod' || CHAR(10) ||
 '- Deployment' || CHAR(10) ||
 '- Service',
 9, 2
),
(
 'Тестирование REST API',
 '# REST API testing' || CHAR(10) ||
 'Тестирование REST включает проверку корректности ответов, кода состояния и структуры JSON.' || CHAR(10) ||
 'Рекомендации:' || CHAR(10) ||
 '- Использовать Postman' || CHAR(10) ||
 '- Писать интеграционные тесты',
 14, 3
),
(
 'Java Collections',
 '# Java Collections' || CHAR(10) ||
 'Коллекции в Java включают List, Set, Map и позволяют хранить и обрабатывать группы объектов.' || CHAR(10) ||
 'Полезные методы:' || CHAR(10) ||
 '- add()' || CHAR(10) ||
 '- remove()' || CHAR(10) ||
 '- stream()',
 7, 1
),
(
 'Spring Security',
 '# Spring Security' || CHAR(10) ||
 'Spring Security обеспечивает аутентификацию и авторизацию для веб-приложений на Spring.' || CHAR(10) ||
 'Основные компоненты:' || CHAR(10) ||
 '- AuthenticationManager' || CHAR(10) ||
 '- Filters' || CHAR(10) ||
 '- UserDetailsService',
 11, 2
),
(
 'Работа с JSON',
 '# JSON в Java' || CHAR(10) ||
 'JSON широко используется для обмена данными между фронтендом и бэкендом. В Java удобно использовать Jackson.' || CHAR(10) ||
 'Советы:' || CHAR(10) ||
 '- ObjectMapper.readValue()' || CHAR(10) ||
 '- ObjectMapper.writeValueAsString()',
 6, 1
),
(
 'Многопоточность в Java',
 '# Concurrency' || CHAR(10) ||
 'Многопоточность позволяет выполнять несколько задач одновременно, используя Thread, ExecutorService и CompletableFuture.' || CHAR(10) ||
 'Советы:' || CHAR(10) ||
 '- Синхронизация ресурсов' || CHAR(10) ||
 '- Использовать ThreadPool',
 13, 3
),
(
 'Логирование в приложениях',
 '# Логирование' || CHAR(10) ||
 'Логи помогают находить ошибки в продакшене и мониторить работу системы.' || CHAR(10) ||
 'Рекомендуется:' || CHAR(10) ||
 '- INFO для бизнес-логики' || CHAR(10) ||
 '- ERROR для исключений' || CHAR(10) ||
 '- DEBUG для отладки',
 12, 2
);


INSERT INTO post_tag VALUES
(1, 1), (1, 2), (1, 3);

INSERT INTO post_tag VALUES
(2, 6), (2, 3), (2, 8);

INSERT INTO post_tag VALUES
(3, 1), (3, 5), (3, 9);

INSERT INTO post_tag VALUES
(4, 4), (4, 2), (4, 9);

INSERT INTO post_tag VALUES
(5, 3), (5, 6), (5, 9);

INSERT INTO post_tag VALUES
(6, 3), (6, 8);

INSERT INTO post_tag VALUES
(7, 7), (7, 1), (7, 3);

INSERT INTO post_tag VALUES
(8, 10), (8, 6), (8, 3);

INSERT INTO post_tag VALUES
(9, 1), (9, 2), (9, 9);

INSERT INTO post_tag VALUES
(10, 9), (10, 3);

INSERT INTO post_tag VALUES
(11, 8), (11, 3);


INSERT INTO comments (text, post_id) VALUES
('Отличное объяснение, стало намного понятнее', 1),
('Полезная статья, спасибо', 1),
('Хотелось бы больше примеров', 5),
('Очень актуальная тема', 6),
('Mockito наконец стал понятен', 7),
('Хорошее введение в безопасность', 8),
('Информация про транзакции очень помогла', 9);


INSERT INTO images (post_id) VALUES (1);
INSERT INTO images (post_id) VALUES (2);
INSERT INTO images (post_id) VALUES (3);
INSERT INTO images (post_id) VALUES (4);
INSERT INTO images (post_id) VALUES (5);
INSERT INTO images (post_id) VALUES (6);
INSERT INTO images (post_id) VALUES (7);
INSERT INTO images (post_id) VALUES (8);
INSERT INTO images (post_id) VALUES (9);
INSERT INTO images (post_id) VALUES (10);
INSERT INTO images (post_id) VALUES (11);
