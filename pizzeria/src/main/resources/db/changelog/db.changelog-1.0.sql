-- liquibase formatted sql

-- changeset natali:1
CREATE TABLE customers (
                           id BIGSERIAL PRIMARY KEY,
                           name VARCHAR(100) NOT NULL,
                           phone VARCHAR(20) NOT NULL,
                           address TEXT NOT NULL,
                           email VARCHAR(100),
                           registration_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                           active BOOLEAN DEFAULT TRUE
);

-- changeset natali:2
CREATE TABLE pizzas (
                        id BIGSERIAL PRIMARY KEY,
                        name VARCHAR(100) NOT NULL,
                        description TEXT,
                        price DOUBLE PRECISION NOT NULL,
                        available BOOLEAN DEFAULT TRUE,
                        image_url VARCHAR(255),
                        cooking_time_min INTEGER DEFAULT 15,
                        category VARCHAR(50)
);

-- changeset natali:3
CREATE TABLE ingredients (
                             id BIGSERIAL PRIMARY KEY,
                             name VARCHAR(100) NOT NULL,
                             stock_quantity DOUBLE PRECISION NOT NULL,
                             unit VARCHAR(20) NOT NULL,
                             cost_per_unit DOUBLE PRECISION NOT NULL
);

-- changeset natali:4
CREATE TABLE pizza_ingredients (
                                   pizza_id BIGINT NOT NULL REFERENCES pizzas(id) ON DELETE CASCADE,
                                   ingredient_id BIGINT NOT NULL REFERENCES ingredients(id) ON DELETE CASCADE,
                                   quantity DOUBLE PRECISION NOT NULL,
                                   PRIMARY KEY (pizza_id, ingredient_id)
);

-- changeset natali:5
CREATE TABLE orders (
                        id BIGSERIAL PRIMARY KEY,
                        customer_id BIGINT NOT NULL REFERENCES customers(id),
                        order_date TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
                        status VARCHAR(50) NOT NULL DEFAULT 'NEW',
                        total_price DOUBLE PRECISION NOT NULL,
                        delivery_address TEXT NOT NULL,
                        delivery_notes TEXT,
                        payment_method VARCHAR(50) NOT NULL,
                        payment_status VARCHAR(50) NOT NULL DEFAULT 'PENDING'
);

-- changeset natali:6
CREATE TABLE order_items (
                             id BIGSERIAL PRIMARY KEY,
                             order_id BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
                             pizza_id BIGINT NOT NULL REFERENCES pizzas(id),
                             quantity INTEGER NOT NULL CHECK (quantity > 0),
                             price DOUBLE PRECISION NOT NULL,
                             special_requests TEXT
);

-- changeset natali:7
CREATE TABLE employees (
                           id BIGSERIAL PRIMARY KEY,
                           name VARCHAR(100) NOT NULL,
                           position VARCHAR(50) NOT NULL,
                           phone VARCHAR(20) NOT NULL,
                           email VARCHAR(100),
                           hire_date DATE NOT NULL,
                           active BOOLEAN DEFAULT TRUE
);

-- changeset natali:8
CREATE TABLE deliveries (
                            id BIGSERIAL PRIMARY KEY,
                            order_id BIGINT NOT NULL REFERENCES orders(id),
                            employee_id BIGINT REFERENCES employees(id),
                            delivery_time TIMESTAMP WITH TIME ZONE,
                            status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
                            estimated_time TIMESTAMP WITH TIME ZONE,
                            CONSTRAINT uk_delivery_order UNIQUE (order_id)  -- Добавлено ограничение уникальности
);

-- changeset natali:9
CREATE TABLE role (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(20) NOT NULL
);

-- changeset natali:10
CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(20) NOT NULL UNIQUE,
                       password VARCHAR(120) NOT NULL,
                       email VARCHAR(50) NOT NULL UNIQUE,
                       deleted BOOLEAN NOT NULL DEFAULT FALSE  -- Добавлено сразу
);

-- changeset natali:11
CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            role_id INT NOT NULL,
                            PRIMARY KEY (user_id, role_id),
                            FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
                            FOREIGN KEY (role_id) REFERENCES role (id) ON DELETE CASCADE
);

-- Далее идут команды для установки CACHE 1 для каждой таблицы
-- changeset natali:12
ALTER SEQUENCE IF EXISTS customers_id_seq CACHE 1;

-- changeset natali:13
ALTER SEQUENCE IF EXISTS pizzas_id_seq CACHE 1;

-- changeset natali:14
ALTER SEQUENCE IF EXISTS ingredients_id_seq CACHE 1;

-- changeset natali:15
ALTER SEQUENCE IF EXISTS orders_id_seq CACHE 1;

-- changeset natali:16
ALTER SEQUENCE IF EXISTS order_items_id_seq CACHE 1;

-- changeset natali:17
ALTER SEQUENCE IF EXISTS employees_id_seq CACHE 1;

-- changeset natali:18
ALTER SEQUENCE IF EXISTS deliveries_id_seq CACHE 1;

-- changeset natali:19
ALTER SEQUENCE IF EXISTS users_id_seq CACHE 1;