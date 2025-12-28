# my-blog-back-app

Бэкенд веб-приложения блога, реализованный на Java 21 с использованием Spring Boot + Java21 + база данных H2.

## ⚙️ Сборка проекта

Проект можно собрать с помощью Maven:

Maven:
`
mvn clean package
`
## 🌐 Запуск бэкенда

Бэкенд доступен локально по адресу:

http://localhost:8080

Для запуска приложения достаточно запустить main метод в SpringBootBlogApplication классе или выполнить команду

`
mvn spring-boot:run
`

## 🧪 Тестирование

Для запуска юнит- и интеграционных тестов:

`mvn test`

Тесты используют JUnit 5 и Spring TestContext Framework.
Результаты тестов можно найти в build/reports/tests/ или target/surefire-reports/.


## 🗄️ База данных

Поддерживается in-memory база данных - H2.
Тестовые данные инициализируются через SQL-скрипты.

## 📡 REST API

### Посты

* Получение списка постов

GET /api/posts?search={search}&pageNumber={pageNumber}&pageSize={pageSize}

Ответ (JSON):
`{
"posts": [
{
"id": 1,
"title": "Название поста 1",
"text": "Текст поста...",
"tags": ["tag_1", "tag_2"],
"likesCount": 5,
"commentsCount": 1
}
],
"hasPrev": true,
"hasNext": false,
"lastPage": 3
}`

* Получение поста

GET /api/posts/{id}

Ответ (JSON):

`{
"id": 1,
"title": "Название поста 1",
"text": "Текст поста...",
"tags": ["tag_1", "tag_2"],
"likesCount": 5,
"commentsCount": 1
}`

* Создание поста

POST /api/posts
Тело запроса:

`{
"title": "Название поста 3",
"text": "Текст поста...",
"tags": ["tag_1", "tag_2"]
}`


Ответ:

`{
"id": 3,
"title": "Название поста 3",
"text": "Текст поста...",
"tags": ["tag_1", "tag_2"],
"likesCount": 0,
"commentsCount": 0
}`

* Редактирование поста

PUT /api/posts/{id}
Тело запроса:

`{
"id": 3,
"title": "Обновленное название",
"text": "Обновленный текст...",
"tags": ["tag_1", "tag_2"]
}`

Ответ:

`{
"id": 3,
"title": "Обновленное название",
"text": "Обновленный текст...",
"tags": ["tag_1", "tag_2"],
"likesCount": 0,
"commentsCount": 0
}`

* Удаление поста

DELETE /api/posts/{id} — удаляет пост со всеми комментариями.

* Лайки

POST /api/posts/{id}/likes — увеличивает количество лайков на 1 и возвращает обновлённое значение.

* Работа с изображением поста

PUT /api/posts/{id}/image — загрузка/обновление картинки (multipart/form-data).

GET /api/posts/{id}/image — получение картинки поста (массив байт).

* Комментарии
Получение всех комментариев поста

GET /api/posts/{post_id}/comments

Ответ:

`[
{
"id": 1,
"text": "Комментарий к посту",
"postId": 1
}
]`

* Получение комментария по ID

GET /api/posts/{post_id}/comments/{id}

Ответ:

`{
"id": 1,
"text": "Комментарий к посту",
"postId": 1
}`

* Создание комментария

POST /api/posts/{post_id}/comments
Тело запроса:

`{
"text": "Комментарий к посту",
"postId": 1
}`


Ответ:

`{
"id": 2,
"text": "Комментарий к посту",
"postId": 1
}`

* Редактирование комментария

PUT /api/posts/{post_id}/comments/{id}
Тело запроса:
`
{
"id": 2,
"text": "Обновлённый комментарий",
"postId": 1
}`


Ответ:

`{
"id": 2,
"text": "Обновлённый комментарий",
"postId": 1
}
`
* Удаление комментария

DELETE /api/posts/{post_id}/comments/{id}
