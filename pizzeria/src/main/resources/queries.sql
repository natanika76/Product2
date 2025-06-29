/*Проверить текущую нумерацию - посмотрите в таблице databasechangelog*/
SELECT id, author, filename, orderexecuted
FROM databasechangelog
ORDER BY orderexecuted;

/*Очистите старые миграции (если они уже частично применились)*/
TRUNCATE TABLE public.databasechangelog;

/* Увидеть записи с хешами паролей*/
SELECT username, password FROM users WHERE deleted = false;

-- Или для конкретного пользователя
UPDATE users SET password = '{bcrypt}$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG'
WHERE username = 'admin';

SELECT username, password FROM users WHERE username = 'admin';

SELECT * FROM users;

ALTER SEQUENCE orders_id_seq CACHE 1;





