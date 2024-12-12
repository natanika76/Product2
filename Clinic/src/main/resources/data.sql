-- Заполнение таблицы patients ТАБЛИЦЫ СОЗДАЮТСЯ ТОЛЬКО ПРИ ЗАПУСКЕ ClinicApplication!!!!!
INSERT INTO patients (first_name, last_name, birth_date, phone_number, email, address)
VALUES ('Иван', 'Попов', '1985-05-15', '+1234567890', 'ivan.popov@example.com', 'Липецк, пр.Мира, д.31 кв.15'),
       ('Мария', 'Кузнецова', '1990-07-20', '+1987654321', 'maria.kuz@example.com', 'Елец, ул.Ленина, д.2 кв.154'),
       ('Алиса', 'Федина', '1975-02-10', '+1123456789', 'alice.fedina@example.com', 'Воронеж, ул.К.Маркса, д.20 кв.43'),
       ('Егор', 'Фомин', '1971-06-09', '+1123356781', 'egor.fomin@example.com', 'Липецк, ул.Катукова, д.28 кв.4'),
       ('Станислав', 'Брунов', '1981-03-11', '+1123656339', 'stas.brunov@example.com',
        'Воронеж, Ленинский пр., д.4 кв.6'),
       ('Борис', 'Жидков', '1983-11-30', '+1123454587', 'boris.zhidkov@example.com', 'Липецк, пер.Рудный, д.3 кв.19'),
       ('Федор', 'Шаляпин', '1950-10-19', '+1123455667', 'fedor.shalapa@example.com',
        'Елец, ул.Октябрьская, д.15/3 кв.10'),
       ('Алла', 'Духова', '1980-10-08', '+1123444770', 'alla.dukhova@example.com', 'Воронеж, пр.Революции, д.7 кв.77'),
       ('Елена', 'Филимонова', '2000-01-13', '+1123413131', 'elena.filimon@example.com',
        'Липецк, ул.Лесная, д.35 кв.66'),
       ('Лев', 'Троцкий', '2002-06-16', '+1123433659', 'lev.tro@example.com', 'Липецк, ул.Гагарина, д.75 кв.3'),
       ('Павел', 'Морозов', '2003-12-01', '+1123469014', 'pavel.morozov@example.com',
        'Липецк, Сиреневый пр., д.120 кв.50')
ON CONFLICT DO NOTHING;;

-- Заполнение таблицы doctors
INSERT INTO doctors (first_name, last_name, specialty, phone_number, email, office_number)
VALUES ('Ева', 'Брунова', 'Кардиолог', '+1234001122', 'eva.brunova@hospital.com', '101'),
       ('Михаил', 'Зуев', 'Дерматолог', '+1234003344', 'michael.zuev@hospital.com', '102'),
       ('Денис', 'Белов', 'Хирург-ортопед', '+1234005566', 'denis.belov@hospital.com', '103'),
       ('Дарья', 'Бирюкова', 'Терапевт', '+1234033789', 'daria.bir@hospital.com', '104'),
       ('Юрий', 'Степанов', 'Стоматолог', '+1234951753', 'yura.stepa@hospital.com', '105'),
       ('Ольга', 'Копылова', 'Эндокринолог', '+1234066587', 'olga.kop@hospital.com', '106'),
       ('Елена', 'Князева', 'Ревматолог', '+1234099843', 'el.kniazeva@hospital.com', '107'),
       ('Михаил', 'Панов', 'Окулист', '+1234011436', 'misha.panov@hospital.com', '108'),
       ('Сергей', 'Володин', 'Рентгенолог', '+1234077410', 'sergei.volodin@hospital.com', '109'),
       ('Олег', 'Попов', 'Диетолог', '+1234054821', 'denis.belov@hospital.com', '110')
ON CONFLICT DO NOTHING;;

-- Заполнение таблицы services
INSERT INTO services (name, description, price)
VALUES ('Общий осмотр', 'Обычный медицинский осмотр.', 500.00),
       ('Кардиологическая консультация', 'Консультация кардиолога с УЗИ', 1500.00),
       ('Уход за кожей', 'Диагностика и профилактика заболеваний кожи.', 1000.00),
       ('Ортопедическая хирургия', 'Хирургическая процедура при ортопедических заболеваниях.', 20000.00),
       ('Направление в стационар', 'Выдача направления на операции в больницы.', 700.00),
       ('Профессиональная чистка', 'Профессиональный уход за полостью рта.', 4500.00),
       ('Лечение пульпита', 'Лечение с применением лекарственных препаратов.', 1750.00),
       ('Эндокринологическая консультация', 'Консультация эндокринолога с измерением роста и веса.', 1300.00),
       ('Консультация ревматолога', 'Консультация ревматолога с выпиской лекарств.', 1600.00),
       ('Консультация окулиста', 'Проверка зрения, выдача заключения', 1100.00),
       ('Консультация рентгенолога', 'Консультация с заключением по рентгену.', 1100.00),
       ('Консультация диетолога', 'Консультация с назначением правильного питания.', 1200.00),
       ('Измерение медпоказателей', 'Измерение давления и пульса.', 300.00),
       ('Измерение температуры', 'Измерение давления и пульса.', 200.00)
ON CONFLICT DO NOTHING;;


-- Заполнение таблицы appointments
INSERT INTO appointments (patient_id, doctor_id, service_id, appointment_date, status)
VALUES (1, 1, 2, '2024-12-04 10:00:00', 'Плановый'),
       (2, 2, 3, '2024-12-06 14:00:00', 'Плановый'),
       (3, 3, 4, '2024-12-07 09:00:00', 'Завершен'),
       (1, 1, 1, '2024-12-02 11:00:00', 'Отменен'),
       (4, 6, 8, '2024-12-06 10:00:00', 'Плановый'),
       (5, 7, 9, '2024-12-02 12:00:00', 'Плановый'),
       (7, 4, 1, '2024-12-13 16:00:00', 'Отменен'),
       (5, 7, 9, '2024-12-07 13:00:00', 'Завершен'),
       (6, 8, 10, '2024-12-09 11:00:00', 'Завершен'),
       (7, 5, 6, '2024-12-09 11:00:00', 'Завершен'),
       (8, 9, 11, '2024-12-10 9:00:00', 'Плановый'),
       (9, 10, 12, '2024-12-04 10:00:00', 'Плановый'),
       (10, 8, 10, '2024-12-12 12:00:00', 'Отменен'),
       (11, 4, 13, '2024-12-07 13:00:00', 'Завершен'),
       (9, 8, 10, '2024-12-05 14:00:00', 'Отменен'),
       (9, 10, 12, '2024-12-18 11:00:00', 'Плановый'),
       (10, 9, 11, '2024-12-10 11:00:00', 'Завершен'),
       (11, 4, 14, '2024-12-07 15:00:00', 'Завершен')
ON CONFLICT DO NOTHING;


-- Заполнение таблицы medical_records
INSERT INTO medical_records (patient_id, doctor_id, diagnosis, treatment, record_date)
VALUES (1, 1, 'Гипертония', 'Назначенные лекарства и изменение образа жизни.', '2024-12-04 10:00:00'),
       (2, 2, 'Угревая сыпь', 'Прописал кремы для местного применения.', '2024-11-25 09:30:00'),
       (3, 3, 'Перелом запястья', 'Хирургическое вмешательство и физиотерапия.', '2024-10-15 08:45:00'),
       (4, 6, 'Болезнь щитовидной железы.', 'Лекарственные средства и УЗИ.', '2024-11-03 09:15:00'),
       (5, 7, 'Недостаток кальция.', 'Рентген костей и лекарственные препараты.', '2024-09-09 18:35:00'),
       (6, 8, 'Глаукома.', 'Подготовка к госпитализации.', '2024-10-10 16:28:00'),
       (7, 5, 'Установка коронки.', 'Лечение, штифты, коронка.', '2024-11-15 13:20:00'),
       (8, 9, 'Рентген легких.', 'Наблюдение коронавируса.', '2024-11-18 16:30:00'),
       (9, 10, 'Избыточный вес.', 'Направление в фитнес-клуб.', '2024-10-18 08:30:00'),
       (10, 8, 'Катаракта.', 'Направление на госпитализацию.', '2024-11-20 18:40:00'),
       (11, 4, 'Общее недомогание.', 'Направление на обследование.', '2024-11-20 18:40:00')
ON CONFLICT DO NOTHING;;

-- Заполнение таблицы schedules
INSERT INTO schedules (doctor_id, day_of_week, start_time, end_time)
VALUES (1, 'Понедельник', '09:00:00', '17:00:00'),
       (1, 'Среда', '09:00:00', '17:00:00'),
       (2, 'Вторник', '10:00:00', '16:00:00'),
       (2, 'Четверг', '10:00:00', '16:00:00'),
       (3, 'Пятница', '09:00:00', '15:00:00'),
       (3, 'Суббота', '09:00:00', '12:00:00'),
       (5, 'Понедельник', '10:00:00', '16:00:00'),
       (6, 'Пятница', '10:00:00', '16:00:00'),
       (7, 'Понедельник', '12:00:00', '16:00:00'),
       (7, 'Суббота', '13:00:00', '17:00:00'),
       (8, 'Понедельник', '10:00:00', '16:00:00'),
       (8, 'Четверг', '10:00:00', '16:00:00'),
       (9, 'Понедельник', '09:00:00', '17:00:00'),
       (10, 'Среда', '09:00:00', '15:00:00'),
       (4, 'Пятница', '13:00:00', '18:00:00'),
       (4, 'Суббота', '12:00:00', '16:00:00')
ON CONFLICT DO NOTHING;
