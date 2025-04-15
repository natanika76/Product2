-- liquibase formatted sql

-- changeset natali:6
CREATE TABLE IF NOT EXISTS courses1_schema.reviews (
                                                       id BIGSERIAL PRIMARY KEY,
                                                       student_id BIGINT NOT NULL,
                                                       course_id BIGINT NOT NULL,
                                                       review_text TEXT NOT NULL,
                                                       rating INT CHECK (rating >= 1 AND rating <= 5),
                                                       created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                                       CONSTRAINT fk_review_student FOREIGN KEY (student_id) REFERENCES courses1_schema.students(id),
                                                       CONSTRAINT fk_review_course FOREIGN KEY (course_id) REFERENCES courses1_schema.courses(id)
);