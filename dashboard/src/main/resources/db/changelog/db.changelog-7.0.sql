-- liquibase formatted sql

-- changeset natali:16
-- Создание пустых колонок
ALTER TABLE courses1_schema.students
    ADD COLUMN email VARCHAR(255),
    ADD COLUMN username VARCHAR(255),
    ADD COLUMN password VARCHAR(128) DEFAULT '{noop}123',
    ADD COLUMN role VARCHAR(32);

-- changeset natali:17
-- Формирование email
UPDATE courses1_schema.students
SET email = LOWER(first_name || '.' || last_name || '@example.com');

-- changeset natali:18
-- Копирование email в username
UPDATE courses1_schema.students
SET username = email;

-- changeset natali:19
-- Установка ограничений NOT NULL
ALTER TABLE courses1_schema.students
    ALTER COLUMN email SET NOT NULL,
    ALTER COLUMN username SET NOT NULL;