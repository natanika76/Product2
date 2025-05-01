-- liquibase formatted sql

-- changeset natali:19
-- Назначение роли "USER" всем существующим пользователям
UPDATE courses1_schema.students
SET role = 'USER';

