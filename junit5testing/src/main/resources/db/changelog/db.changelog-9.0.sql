-- liquibase formatted sql

-- changeset natali:21
-- Миграция существующих данных изменение на LocalDate
ALTER TABLE courses1_schema.courses ALTER COLUMN start_date TYPE date USING start_date::date;

