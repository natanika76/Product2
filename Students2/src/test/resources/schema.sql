CREATE TABLE IF NOT EXISTS students (
                                        id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
                                        full_name VARCHAR(255) NOT NULL,
                                        email VARCHAR(100) UNIQUE NOT NULL,
                                        courses VARCHAR(1000) -- Хранение курсов в виде строки с разделителями
);