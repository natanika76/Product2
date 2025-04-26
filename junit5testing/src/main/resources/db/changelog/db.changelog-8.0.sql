-- liquibase formatted sql

-- changeset natali:20
-- Назначение роли "USER" всем существующим пользователям
UPDATE courses1_schema.students
SET role = 'USER';

