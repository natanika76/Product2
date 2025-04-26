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
-- Наполнение таблицы students тестовыми студентами (transliterated names)
INSERT INTO courses1_schema.students (first_name, last_name, course_id)
VALUES
    ('Ivan', 'Ivanov', 1),
    ('Petr', 'Petrov', 1),
    ('Mariya', 'Sidorova', 2),
    ('Aleksey', 'Smirnov', 2),
    ('Elena', 'Kuznetsova', 3),
    ('Dmitriy', 'Popov', 3),
    ('Olga', 'Lebedeva', 4),
    ('Sergey', 'Kozlov', 4),
    ('Anna', 'Novikova', 5),
    ('Andrey', 'Morozov', 5),
    ('Natalya', 'Volkova', 6),
    ('Artem', 'Solovyev', 6),
    ('Viktoriya', 'Vasileva', 7),
    ('Igor', 'Zaytsev', 7),
    ('Yuliya', 'Pavlova', null); -- Студент без курса