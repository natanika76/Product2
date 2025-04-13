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