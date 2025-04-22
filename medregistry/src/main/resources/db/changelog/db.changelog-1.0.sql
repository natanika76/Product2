-- liquibase formatted sql

-- changeset author:1
-- Create tables
CREATE TABLE role (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(20) NOT NULL
);

CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       username VARCHAR(20) NOT NULL UNIQUE,
                       password VARCHAR(120) NOT NULL,
                       email VARCHAR(50) NOT NULL UNIQUE,
                       deleted BOOLEAN NOT NULL DEFAULT FALSE  -- Добавлено сразу
);

CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            role_id INT NOT NULL,
                            PRIMARY KEY (user_id, role_id),
                            FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
                            FOREIGN KEY (role_id) REFERENCES role (id) ON DELETE CASCADE
);

CREATE TABLE doctor (
                        id BIGSERIAL PRIMARY KEY,
                        first_name VARCHAR(50) NOT NULL,
                        last_name VARCHAR(50) NOT NULL,
                        specialization VARCHAR(100) NOT NULL,
                        deleted BOOLEAN NOT NULL DEFAULT FALSE  -- Добавлено сразу
);

CREATE TABLE patient (
                         id BIGSERIAL PRIMARY KEY,
                         first_name VARCHAR(50) NOT NULL,
                         last_name VARCHAR(50) NOT NULL,
                         date_of_birth DATE NOT NULL,
                         insurance_number VARCHAR(20) UNIQUE,
                         deleted BOOLEAN NOT NULL DEFAULT FALSE  -- Добавлено сразу
);

-- changeset author:2
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

-- Insert doctors
INSERT INTO doctor (first_name, last_name, specialization) VALUES ('Иван', 'Потапов', 'кардиолог');
INSERT INTO doctor (first_name, last_name, specialization) VALUES ('Эмилия', 'Жданова', 'нейрохирург');
INSERT INTO doctor (first_name, last_name, specialization) VALUES ('Михаил', 'Михайлов', 'педиатр');

-- Insert patients
INSERT INTO patient (first_name, last_name, date_of_birth, insurance_number) VALUES ('Роман', 'Брунов', '1985-05-15', 'INS123456');
INSERT INTO patient (first_name, last_name, date_of_birth, insurance_number) VALUES ('Евгения', 'Давидова', '1990-08-22', 'INS789012');
INSERT INTO patient (first_name, last_name, date_of_birth, insurance_number) VALUES ('Борис', 'Агафонов', '1978-03-10', 'INS345678');