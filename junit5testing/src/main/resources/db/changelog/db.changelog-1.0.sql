-- liquibase formatted sql

-- changeset natali:1
CREATE TABLE IF NOT EXISTS courses1_schema.courses (
                                                      id BIGSERIAL PRIMARY KEY,
                                                      name VARCHAR(255) NOT NULL,
                                                      start_date DATE NOT NULL,
                                                      is_active BOOLEAN NOT NULL DEFAULT TRUE
);

-- changeset natali:2
CREATE TABLE IF NOT EXISTS courses1_schema.students (
                                                        id BIGSERIAL PRIMARY KEY,  -- BIGSERIAL для Long (8 байт)
                                                        first_name VARCHAR(255) NOT NULL,
                                                        last_name VARCHAR(255) NOT NULL,
                                                        course_id BIGINT,  -- BIGINT для соответствия с BIGSERIAL (если course_id ссылается на другой BIGSERIAL)
                                                        CONSTRAINT fk_student_course
                                                            FOREIGN KEY (course_id)
                                                                REFERENCES courses1_schema.courses(id)
                                                                ON DELETE SET NULL
);

