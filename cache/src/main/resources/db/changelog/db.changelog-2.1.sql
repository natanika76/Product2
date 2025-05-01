-- liquibase formatted sql

-- changeset natali:6
-- Добавление столбца info в таблицу students
ALTER TABLE courses1_schema.students
    ADD COLUMN info VARCHAR(128);