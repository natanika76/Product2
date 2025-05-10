select *
from patients;
select *
from doctors;
select *
from services;
select *
from appointments;
select *
from medical_records;
select *
from schedules;

/*drop table appointments;
drop table services;
drop table medical_records;
drop table patients;
drop table schedules;
drop table doctors;*/

-- Поиск всех приемов с подробной информацией о пациенте, враче и услуге
WITH detailed_appointments AS (SELECT a.id                               AS appointment_id,
                                      p.first_name || ' ' || p.last_name AS patient_name,
                                      d.first_name || ' ' || d.last_name AS doctor_name,
                                      s.name                             AS service_name,
                                      a.appointment_date,
                                      a.status
                               FROM appointments a
                                        JOIN patients p ON a.patient_id = p.id
                                        JOIN doctors d ON a.doctor_id = d.id
                                        JOIN services s ON a.service_id = s.id)
SELECT *
FROM detailed_appointments;

--  Получение расписания всех врачей с указанием их специализации
WITH doctor_schedules AS (SELECT d.first_name || ' ' || d.last_name AS doctor_name,
                                 d.specialty,
                                 s.day_of_week,
                                 s.start_time,
                                 s.end_time
                          FROM doctors d
                                   JOIN schedules s ON d.id = s.doctor_id)
SELECT *
FROM doctor_schedules;

-- Поиск медицинских записей по пациенту с указанием врача
WITH patient_records AS (SELECT mr.id                              AS record_id,
                                p.first_name || ' ' || p.last_name AS patient_name,
                                d.first_name || ' ' || d.last_name AS doctor_name,
                                mr.diagnosis,
                                mr.treatment,
                                mr.record_date
                         FROM medical_records mr
                                  JOIN patients p ON mr.patient_id = p.id
                                  LEFT JOIN doctors d ON mr.doctor_id = d.id)
SELECT *
FROM patient_records
WHERE patient_name = 'Алиса Федина';

-- Получение дохода клиники за определенный период
WITH income_data AS (SELECT s.name       AS service_name,
                            COUNT(a.id)  AS service_count,
                            SUM(s.price) AS total_income
                     FROM appointments a
                              JOIN services s ON a.service_id = s.id
                     WHERE a.status = 'Завершен'
                       AND a.appointment_date BETWEEN '2024-01-01' AND '2024-12-31'
                     GROUP BY s.name)
SELECT *
FROM income_data
ORDER BY total_income DESC;

-- Общая сумма дохода от всех услуг за указанный период
-- Подзапрос total_income_summary суммирует все доходы из первого подзапроса, получая общую сумму доходов за период
-- Основной запрос объединяет результаты двух подзапросов с помощью оператора CROSS JOIN, добавляя столбец с общим доходом КО ВСЕМ СТРОКАМ результата
WITH income_data AS (SELECT s.name       AS service_name,
                            COUNT(a.id)  AS service_count,
                            SUM(s.price) AS total_income
                     FROM appointments a
                              JOIN services s ON a.service_id = s.id
                     WHERE a.status = 'Завершен'
                       AND a.appointment_date BETWEEN '2024-01-01' AND '2024-12-31'
                     GROUP BY s.name),
     total_income_summary AS (SELECT SUM(total_income) AS grand_total
                              FROM income_data)
SELECT i.service_name,
       i.service_count,
       i.total_income,
       t.grand_total
FROM income_data i
         CROSS JOIN
     total_income_summary t
ORDER BY i.total_income DESC;

