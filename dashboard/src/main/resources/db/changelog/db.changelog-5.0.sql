-- liquibase formatted sql

-- changeset natali:12
-- Создание таблицы для связи многие-ко-многим между студентами и курсами
CREATE TABLE IF NOT EXISTS courses1_schema.student_courses (
                                                               student_id BIGINT NOT NULL,
                                                               course_id BIGINT NOT NULL,
                                                               enrolled_at TIMESTAMP WITHOUT TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                                                               CONSTRAINT pk_student_course PRIMARY KEY (student_id, course_id),
                                                               CONSTRAINT fk_student FOREIGN KEY (student_id) REFERENCES courses1_schema.students(id) ON DELETE CASCADE,
                                                               CONSTRAINT fk_course FOREIGN KEY (course_id) REFERENCES courses1_schema.courses(id) ON DELETE CASCADE
);

-- changeset natali:13
-- Добавление комментариев к таблице и колонкам
COMMENT ON TABLE courses1_schema.student_courses IS 'Таблица для связи студентов с курсами (многие-ко-многим)';
COMMENT ON COLUMN courses1_schema.student_courses.enrolled_at IS 'Дата и время записи студента на курс';

-- changeset natali:14
-- Тестовые данные: запись студентов на несколько курсов
INSERT INTO courses1_schema.student_courses (student_id, course_id, enrolled_at)
VALUES
    -- Студент 1 (Иван Иванов) записан на 3 курса
    (1, 1, NOW()),
    (1, 2, NOW()),
    (1, 3, NOW()),

    -- Студент 2 (Петр Петров) записан на 2 курса
    (2, 1, NOW()),
    (2, 4, NOW()),

    -- Студент 3 (Мария Сидорова) записана на 4 курса
    (3, 2, NOW()),
    (3, 5, NOW()),
    (3, 6, NOW()),
    (3, 7, NOW()),

    -- Студент 15 (Юлия Павлова) без основного курса, но с 2 дополнительными
    (15, 3, NOW()),
    (15, 5, NOW());