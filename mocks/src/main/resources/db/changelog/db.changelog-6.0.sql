-- liquibase formatted sql

-- changeset natali:14
-- Удаление столбца info из таблицы students
ALTER TABLE courses1_schema.students
    DROP COLUMN info;