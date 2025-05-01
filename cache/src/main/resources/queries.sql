/*Изменила в сущности ссылку на столбец */
ALTER TABLE courses_schema.courses ADD COLUMN active boolean NOT NULL DEFAULT true;

/*Проверить текущую нумерацию - посмотрите в таблице databasechangelog*/
SELECT id, author, filename, orderexecuted
FROM databasechangelog
ORDER BY orderexecuted;

/*Очистите старые миграции (если они уже частично применились)*/
TRUNCATE TABLE public.databasechangelog;

/*Проверьте текущую структуру таблицы:*/
SELECT * FROM information_schema.columns
WHERE table_schema = 'courses1_schema' AND table_name = 'courses';

-- Проверка записей для студента с id=1
SELECT s.first_name, s.last_name, c.name AS course_name, sc.enrolled_at
FROM courses1_schema.students s
         JOIN courses1_schema.student_courses sc ON s.id = sc.student_id
         JOIN courses1_schema.courses c ON sc.course_id = c.id
WHERE s.id = 1;

-- Проверка всех записей
SELECT s.id, s.first_name, c.id, c.name, sc.enrolled_at
FROM courses1_schema.student_courses sc
         JOIN courses1_schema.students s ON sc.student_id = s.id
         JOIN courses1_schema.courses c ON sc.course_id = c.id
ORDER BY s.id;



--Узнаем какие последние id были в таблицах
SELECT last_value FROM courses1_schema.courses_id_seq;
SELECT last_value FROM courses1_schema.students_id_seq;
-- Устанавливаем нормальный инкремент идентификатора
SELECT setval('courses1_schema.courses_id_seq', (SELECT MAX(id) + 1 FROM courses1_schema.courses));

