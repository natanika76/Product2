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

-- Комментарий к таблице пациентов
COMMENT ON TABLE patients IS 'Таблица для хранения информации о пациентах клиники.';

-- Комментарии к полям таблицы пациентов
COMMENT ON COLUMN patients.first_name IS 'Имя пациента.';
COMMENT ON COLUMN patients.last_name IS 'Фамилия пациента.';
COMMENT ON COLUMN patients.birth_date IS 'Дата рождения пациента.';
COMMENT ON COLUMN patients.phone_number IS 'Номер телефона пациента.';
COMMENT ON COLUMN patients.email IS 'Адрес электронной почты пациента.';
COMMENT ON COLUMN patients.address IS 'Адрес проживания пациента.';

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

-- Комментарий к таблице врачей
COMMENT ON TABLE doctors IS 'Таблица для хранения информации о врачах клиники.';

-- Комментарии к полям таблицы врачей
COMMENT ON COLUMN doctors.first_name IS 'Имя врача.';
COMMENT ON COLUMN doctors.last_name IS 'Фамилия врача.';
COMMENT ON COLUMN doctors.specialty IS 'Специальность врача.';
COMMENT ON COLUMN doctors.phone_number IS 'Номер телефона врача.';
COMMENT ON COLUMN doctors.email IS 'Адрес электронной почты врача.';
COMMENT ON COLUMN doctors.office_number IS 'Номер кабинета врача.';

-- Медицинские услуги
CREATE TABLE IF NOT EXISTS services
(
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100)   NOT NULL,
    description TEXT,
    price       NUMERIC(10, 2) NOT NULL
);

-- Комментарий к таблице медицинских услуг
COMMENT ON TABLE services IS 'Таблица для хранения информации о предоставляемых медицинских услугах.';

-- Комментарии к полям таблицы медицинских услуг
COMMENT ON COLUMN services.name IS 'Название медицинской услуги.';
COMMENT ON COLUMN services.description IS 'Описание медицинской услуги.';
COMMENT ON COLUMN services.price IS 'Стоимость медицинской услуги.';

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

-- Комментарий к таблице приемов
COMMENT ON TABLE appointments IS 'Таблица для хранения информации о назначенных приемах.';

-- Комментарии к полям таблицы приемов
COMMENT ON COLUMN appointments.patient_id IS 'Идентификатор пациента.';
COMMENT ON COLUMN appointments.doctor_id IS 'Идентификатор врача.';
COMMENT ON COLUMN appointments.service_id IS 'Идентификатор медицинской услуги.';
COMMENT ON COLUMN appointments.appointment_date IS 'Дата и время приема.';
COMMENT ON COLUMN appointments.status IS 'Статус приема ("Плановый", "Завершен", "Отменен").';

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

-- Комментарий к таблице медицинских записей
COMMENT ON TABLE medical_records IS 'Таблица для хранения медицинских записей пациентов.';

-- Комментарии к полям таблицы медицинских записей
COMMENT ON COLUMN medical_records.patient_id IS 'Идентификатор пациента.';
COMMENT ON COLUMN medical_records.doctor_id IS 'Идентификатор врача.';
COMMENT ON COLUMN medical_records.diagnosis IS 'Диагноз.';
COMMENT ON COLUMN medical_records.treatment IS 'Лечение.';
COMMENT ON COLUMN medical_records.record_date IS 'Дата записи.';

-- Расписание врачей
CREATE TABLE IF NOT EXISTS schedules
(
    id          BIGSERIAL PRIMARY KEY,
    doctor_id   INT         NOT NULL REFERENCES doctors (id) ON DELETE CASCADE,
    day_of_week VARCHAR(15) NOT NULL, -- Возможные значения: 'Monday', 'Tuesday', ...
    start_time  TIME        NOT NULL,
    end_time    TIME        NOT NULL
);

-- Комментарий к таблице расписания врачей
COMMENT ON TABLE schedules IS 'Таблица для хранения расписания работы врачей.';

-- Комментарии к полям таблицы расписания врачей
COMMENT ON COLUMN schedules.doctor_id IS 'Идентификатор врача.';
COMMENT ON COLUMN schedules.day_of_week IS 'День недели.';
COMMENT ON COLUMN schedules.start_time IS 'Время начала рабочего дня.';
COMMENT ON COLUMN schedules.end_time IS 'Время окончания рабочего дня.';
