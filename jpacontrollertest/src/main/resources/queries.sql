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

-- Включите поддержку транслитерации в PostgreSQL:
CREATE EXTENSION IF NOT EXISTS unaccent;

-- Создание собственной функции транслитерации на PostgreSQL
CREATE OR REPLACE FUNCTION transliterate(text) RETURNS text AS

$$
DECLARE
    input_text ALIAS FOR $1;
BEGIN
    RETURN lower(regexp_replace(input_text, '(ё|й|ц|у|к|е|н|г|ш|щ|з|х|ъ|ф|ы|в|а|п|р|о|л|д|ж|э|я|ч|с|м|и|т|б|ю)',
                                E'y|\x04|c|u|k|e|n|g|sh|sch|z|x||f|i|v|a|p|r|o|l|d|zh|e|ya|ch|s|m|i|t|b|yu'));
END;

$$ LANGUAGE plpgsql IMMUTABLE STRICT;

