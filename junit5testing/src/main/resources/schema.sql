CREATE SCHEMA IF NOT EXISTS courses_schema;

CREATE TABLE IF NOT EXISTS courses_schema.courses (
                                                      id SERIAL PRIMARY KEY,
                                                      name VARCHAR(255) NOT NULL,
                                                      start_date DATE NOT NULL,
                                                      is_active BOOLEAN NOT NULL DEFAULT TRUE
);


