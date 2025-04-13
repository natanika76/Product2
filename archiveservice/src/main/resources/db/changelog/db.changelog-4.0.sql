-- liquibase formatted sql

-- changeset natali:10
ALTER TABLE courses1_schema.courses
    ADD COLUMN is_archived BOOLEAN NOT NULL DEFAULT FALSE;

-- changeset natali:11
COMMENT ON COLUMN courses1_schema.courses.is_archived IS 'Флаг, указывающий что курс архивирован (дата начала прошла)';