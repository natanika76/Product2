-- liquibase formatted sql

-- changeset natali:15
-- Удаление столбца info из таблицы students
ALTER TABLE courses1_schema.students
    DROP COLUMN info;