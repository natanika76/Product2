-- SQL-запрос для просмотра комментариев:
/*SELECT col_description('orders'::regclass, ordinal_position) AS column_comment
FROM information_schema.columns
WHERE table_name = 'orders';*/

SELECT * FROM orders;
