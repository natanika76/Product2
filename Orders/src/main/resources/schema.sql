CREATE TABLE IF NOT EXISTS orders (
                                      order_id BIGSERIAL PRIMARY KEY,
                                      product_article VARCHAR(50) NOT NULL,
                                      quantity INT NOT NULL,
                                      total_amount DECIMAL(10, 2) NOT NULL,
                                      order_date TIMESTAMP(0) NOT NULL
);

COMMENT ON TABLE orders IS 'Таблица для хранения информации о заказах в интернет-магазине. Включает идентификатор заказа, артикул товара, количество, общую сумму и дату заказа.';

COMMENT ON COLUMN orders.order_id IS 'Уникальный идентификатор заказа. Автоинкрементируемое поле.';
COMMENT ON COLUMN orders.product_article IS 'Артикул товара, который был заказан.';
COMMENT ON COLUMN orders.quantity IS 'Количество единиц товара в заказе.';
COMMENT ON COLUMN orders.total_amount IS 'Общая сумма заказа в валюте (десятичное число с двумя знаками после запятой).';
COMMENT ON COLUMN orders.order_date IS 'Дата и время оформления заказа.';