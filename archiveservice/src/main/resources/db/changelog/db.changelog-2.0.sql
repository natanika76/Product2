-- liquibase formatted sql

-- changeset natali:3
-- Наполнение таблицы courses тестовыми курсами по программированию
INSERT INTO courses1_schema.courses (name, start_date, is_active)
VALUES
    ('Java для начинающих', '2024-03-01', true),
    ('Python: продвинутый уровень', '2024-04-15', true),
    ('Веб-разработка на JavaScript', '2024-05-10', true),
    ('Основы SQL и баз данных', '2024-06-01', true),
    ('Мобильная разработка на Kotlin', '2024-07-20', true),
    ('Машинное обучение на Python', '2024-08-05', true),
    ('DevOps: Docker и Kubernetes', '2024-09-12', true);

-- changeset natali:4
-- Наполнение таблицы students тестовыми студентами
INSERT INTO courses1_schema.students (first_name, last_name, course_id)
VALUES
    ('Иван', 'Иванов', 1),
    ('Петр', 'Петров', 1),
    ('Мария', 'Сидорова', 2),
    ('Алексей', 'Смирнов', 2),
    ('Елена', 'Кузнецова', 3),
    ('Дмитрий', 'Попов', 3),
    ('Ольга', 'Лебедева', 4),
    ('Сергей', 'Козлов', 4),
    ('Анна', 'Новикова', 5),
    ('Андрей', 'Морозов', 5),
    ('Наталья', 'Волкова', 6),
    ('Артем', 'Соловьев', 6),
    ('Виктория', 'Васильева', 7),
    ('Игорь', 'Зайцев', 7),
    ('Юлия', 'Павлова', null);  -- Студент без курса