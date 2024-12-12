-- Пациенты  ТАБЛИЦЫ СОЗДАЮТСЯ ТОЛЬКО ПРИ ЗАПУСКЕ ClinicApplication!!!!!
CREATE TABLE IF NOT EXISTS patients
(
    id           BIGSERIAL PRIMARY KEY,
    first_name   VARCHAR(50) NOT NULL,
    last_name    VARCHAR(50) NOT NULL,
    birth_date   DATE        NOT NULL,
    phone_number VARCHAR(20),
    email        VARCHAR(100),
    address      TEXT
);

-- Врачи
CREATE TABLE IF NOT EXISTS doctors
(
    id            BIGSERIAL PRIMARY KEY,
    first_name    VARCHAR(50)  NOT NULL,
    last_name     VARCHAR(50)  NOT NULL,
    specialty     VARCHAR(100) NOT NULL,
    phone_number  VARCHAR(20),
    email         VARCHAR(100),
    office_number VARCHAR(10)
);

-- Медицинские услуги
CREATE TABLE IF NOT EXISTS services
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100)   NOT NULL,
    description TEXT,
    price       NUMERIC(10, 2) NOT NULL
);

-- Приемы
CREATE TABLE IF NOT EXISTS appointments
(
    id               BIGSERIAL PRIMARY KEY,
    patient_id       INT       NOT NULL REFERENCES patients (id) ON DELETE CASCADE,
    doctor_id        INT       NOT NULL REFERENCES doctors (id) ON DELETE CASCADE,
    service_id       INT       NOT NULL REFERENCES services (id) ON DELETE CASCADE,
    appointment_date TIMESTAMP NOT NULL,
    status           VARCHAR(50) DEFAULT 'Scheduled' -- Возможные значения: 'Плановый', 'Завершен', 'Отменен'
);

-- Медицинские записи
CREATE TABLE IF NOT EXISTS medical_records
(
    id          BIGSERIAL PRIMARY KEY,
    patient_id  INT  NOT NULL REFERENCES patients (id) ON DELETE CASCADE,
    doctor_id   INT  NOT NULL REFERENCES doctors (id) ON DELETE SET NULL,
    diagnosis   TEXT NOT NULL,
    treatment   TEXT,
    record_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Расписание врачей
CREATE TABLE IF NOT EXISTS schedules
(
    id          BIGSERIAL PRIMARY KEY,
    doctor_id   INT         NOT NULL REFERENCES doctors (id) ON DELETE CASCADE,
    day_of_week VARCHAR(15) NOT NULL, -- Возможные значения: 'Monday', 'Tuesday', ...
    start_time  TIME        NOT NULL,
    end_time    TIME        NOT NULL
);

