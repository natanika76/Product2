-- liquibase formatted sql

-- changeset natali:20
-- Заполнение таблицы customers
INSERT INTO customers (name, phone, address, email) VALUES
                                                        ('Иванов Петр Сергеевич', '+79161234567', 'г. Москва, ул. Ленина, д. 10, кв. 25', 'ivanov@example.com'),
                                                        ('Смирнова Анна Владимировна', '+79261234567', 'г. Москва, пр-т Мира, д. 42, кв. 12', 'smirnova@example.com'),
                                                        ('Кузнецов Дмитрий Иванович', '+79031234567', 'г. Москва, ул. Пушкина, д. 5', 'kuznetsov@example.com');

-- changeset natali:21
-- Заполнение таблицы ingredients
INSERT INTO ingredients (name, stock_quantity, unit, cost_per_unit) VALUES
                                                                        ('Тесто для пиццы', 50.0, 'кг', 120.50),
                                                                        ('Томатный соус', 30.0, 'л', 85.00),
                                                                        ('Сыр Моцарелла', 25.0, 'кг', 450.00),
                                                                        ('Ветчина', 15.0, 'кг', 380.00),
                                                                        ('Грибы шампиньоны', 10.0, 'кг', 220.00),
                                                                        ('Пепперони', 12.0, 'кг', 520.00),
                                                                        ('Оливки', 8.0, 'кг', 310.00),
                                                                        ('Томаты', 20.0, 'кг', 180.00);

-- changeset natali:22
-- Заполнение таблицы pizzas
INSERT INTO pizzas (name, description, price, image_url, category) VALUES
                                                                       ('Маргарита', 'Классическая пицца с томатным соусом и сыром моцарелла', 450.00, 'margherita.jpg', 'Классические'),
                                                                       ('Пепперони', 'Острая пицца с колбасками пепперони и сыром', 550.00, 'pepperoni.jpg', 'Острые'),
                                                                       ('Гавайская', 'Пицца с ветчиной и ананасами', 580.00, 'hawaiian.jpg', 'Экзотические'),
                                                                       ('Четыре сыра', 'Пицца с сырами моцарелла, пармезан, дор блю и чеддер', 650.00, 'four_cheese.jpg', 'Сырные'),
                                                                       ('Грибная', 'Пицца с шампиньонами и сыром', 520.00, 'mushroom.jpg', 'Вегетарианские');

-- changeset natali:23
-- Заполнение таблицы pizza_ingredients
INSERT INTO pizza_ingredients (pizza_id, ingredient_id, quantity) VALUES
                                                                      (1, 1, 0.3), (1, 2, 0.05), (1, 3, 0.2),  -- Маргарита
                                                                      (2, 1, 0.3), (2, 2, 0.05), (2, 3, 0.2), (2, 6, 0.15),  -- Пепперони
                                                                      (3, 1, 0.3), (3, 2, 0.05), (3, 3, 0.2), (3, 4, 0.15),  -- Гавайская
                                                                      (4, 1, 0.3), (4, 2, 0.05), (4, 3, 0.25),  -- Четыре сыра (упрощенно)
                                                                      (5, 1, 0.3), (5, 2, 0.05), (5, 3, 0.2), (5, 5, 0.15);  -- Грибная

-- changeset natali:24
-- Заполнение таблицы employees
INSERT INTO employees (name, position, phone, email, hire_date) VALUES
                                                                    ('Соколов Алексей Викторович', 'Пиццамейкер', '+79161112233', 'sokolov@example.com', '2022-01-15'),
                                                                    ('Петрова Елена Михайловна', 'Курьер', '+79162223344', 'petrova@example.com', '2022-03-10'),
                                                                    ('Васильев Игорь Николаевич', 'Менеджер', '+79163334455', 'vasilev@example.com', '2021-11-05');

-- changeset natali:25
-- Заполнение таблицы orders
INSERT INTO orders (customer_id, status, total_price, delivery_address, payment_method, payment_status) VALUES
                                                                                                            (1, 'DELIVERED', 1450.00, 'г. Москва, ул. Ленина, д. 10, кв. 25', 'CARD', 'PAID'),
                                                                                                            (2, 'IN_PROGRESS', 1100.00, 'г. Москва, пр-т Мира, д. 42, кв. 12', 'CASH', 'PENDING'),
                                                                                                            (3, 'NEW', 650.00, 'г. Москва, ул. Пушкина, д. 5', 'CARD', 'PAID');

-- changeset natali:26
-- Заполнение таблицы order_items
INSERT INTO order_items (order_id, pizza_id, quantity, price, special_requests) VALUES
                                                                                    (1, 2, 2, 550.00, 'Очень острая'),
                                                                                    (1, 3, 1, 580.00, 'Без ананасов'),
                                                                                    (2, 1, 1, 450.00, NULL),
                                                                                    (2, 5, 1, 520.00, 'Дополнительно грибы'),
                                                                                    (3, 4, 1, 650.00, NULL);

-- changeset natali:27
-- Заполнение таблицы deliveries
INSERT INTO deliveries (order_id, employee_id, delivery_time, status, estimated_time) VALUES
                                                                                          (1, 2, '2023-05-15 19:30:00+03', 'COMPLETED', '2023-05-15 19:15:00+03'),
                                                                                          (2, NULL, NULL, 'PROCESSING', '2023-05-16 20:00:00+03'),
                                                                                          (3, NULL, NULL, 'PENDING', NULL);

-- changeset author:28
-- Insert roles
INSERT INTO role(name) VALUES('ROLE_USER');
INSERT INTO role(name) VALUES('ROLE_ADMIN');

-- Insert users
-- Password: user
INSERT INTO users (username, password, email) VALUES ('user', '$2a$10$j8oZuNnPK.Xgd7F4x8GgRep741WE2k7C24r.MUlNzaFNl95rFMU62', 'user@example.com');
-- Password: admin
INSERT INTO users (username, password, email) VALUES ('admin', '$2a$10$hewIDs2Ir906oXst0mzaQORxuRpNraIAjtuCdw63kQdpNv.JPT.S6', 'admin@example.com');

-- Insert user roles
INSERT INTO user_roles (user_id, role_id) VALUES (1, 1); -- user has USER role
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2); -- admin has ADMIN role