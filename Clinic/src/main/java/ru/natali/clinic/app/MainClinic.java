package ru.natali.clinic.app;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.natali.clinic.model.*;
import ru.natali.clinic.repository.*;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;

public class MainClinic {

    public static void main(String[] args) {
        //Объект DataSource предоставляет Connection(интерфейс), который предоставляет доступ к БД
        DataSource dataSource = new DriverManagerDataSource("",
                "", "");

        PatientRepository patientRepository = new PatientRepositoryJdbcTemplateImpl(dataSource);
        DoctorRepository doctorRepository = new DoctorRepositoryJdbcTemplateImpl(dataSource);
        MedicalRecordsRepository medicalRecordsRepository = new MedicalRecordsRepositoryJdbcTemplateImpl(dataSource);
        MedserviceRepository medserviceRepository = new MedserviceRepositoryJdbcTemplateImpl(dataSource);
        AppointmentRepository appointmentRepository = new AppointmentRepositoryJdbcTemplateImpl(dataSource);
        ScheduleRepository scheduleRepository = new ScheduleRepositoryJdbcTemplateImpl(dataSource);

        System.out.println(" Р А С П И С А Н И Я");
        //Список всех расписаний
        System.out.println("Список всех расписаний: ");
        scheduleRepository.findAll().forEach(System.out::println);

        //Поиск расписания по id
        System.out.println("Расписание по идентификационному номеру: ");
        System.out.println(scheduleRepository.find(5L));

        //Поиск расписания по дню недели
        System.out.println("Список расписания по дню недели 'Понедельник': ");
        System.out.println(scheduleRepository.findAllByDayOfWeek("Понедельник"));

        //Создание невой записи расписания
        Doctor doctor2 = new Doctor(2L); // Вот для этого нужен конструктор с id
        LocalTime startTime = LocalTime.of(8, 40);
        LocalTime endTime = LocalTime.of(19, 52);
        LocalTime endTimeNew = LocalTime.of(16, 38);

        Schedule schedule = new Schedule(18L, doctor2, "Воскресенье", startTime, endTime);
        //scheduleRepository.save(schedule);

        //Обновление нового расписания
        schedule.setEndTime(endTimeNew);
        //scheduleRepository.update(schedule);

        //Удаление расписания по id
        //scheduleRepository.delete(18L);

        System.out.println("П Р И Е М Ы");
        //Список всех приемов
        System.out.println("Список всех приемов: ");
        appointmentRepository.findAll().forEach(System.out::println);

        //Поиск приема по id
        System.out.println("Прием по идентификационному номеру: ");
        System.out.println(appointmentRepository.find(3L));

        //Поиск приема по статусу
        System.out.println("Список приемов со статусом 'Плановый': ");
        System.out.println(appointmentRepository.findByStatus("Плановый"));

        //Фиксация нового приема
        Patient patient1 = new Patient(4L); // Вот для этого нужен конструктор с id
        Doctor doctor1 = new Doctor(2L); // Вот для этого нужен конструктор с id
        Medservice medservice1 = new Medservice(1L);
        LocalDateTime specificDateTime1 = LocalDateTime.of(2024, Month.DECEMBER, 10, 11, 45);
        Appointment appointment = new Appointment(19L, patient1, doctor1, medservice1, specificDateTime1, "Плановый");
        //appointmentRepository.save(appointment);

        //Обновление данных нового приема
        appointment.setStatus("Завершен");
        //appointmentRepository.update(appointment);

        //Удаление приема
        //appointmentRepository.delete(19L);

         System.out.println(" М Е Д У С Л У Г И");
        //Все медуслуги:
        System.out.println("Все виды медицинских услуг: ");
        medserviceRepository.findAll().forEach(System.out::println);

        //Поиск медуслуги по id
        System.out.println("Медицинская услуга по идентификационному номеру: ");
        System.out.println(medserviceRepository.find(7L));

        //Поиск медуслуги по названию
        System.out.println("Медуслуга по названию сервиса: ");
        System.out.println(medserviceRepository.findByName("Уход за кожей"));

        //Создание новой медуслуги
        Medservice medservice = new Medservice(15L, "Массаж", "Массирование отдельных областей.", 950.00);
        //medserviceRepository.save(medservice);

        //Обновление новой медуслуги
        medservice.setPrice(1233.40);
       // medserviceRepository.update(medservice);

        //Удаление медсервиса по id
        //medserviceRepository.delete(15L);

         System.out.println(" З А П И С И");
        //Выводит список всех записей - выяснить, как здесь вывести данные по доктору и пациенту - там null сейчас
        System.out.println("Все медицинские записи: ");
        medicalRecordsRepository.findAll().forEach(System.out::println);

        //Поиск записи по id
        System.out.println("Запись по id: ");
        System.out.println(medicalRecordsRepository.find(3L));

        // Создание новых объектов Patient и Doctor - СОЗДАЕТ!!!
        Patient patient = new Patient(9L); // Вот для этого нужен конструктор с id
        Doctor doctor = new Doctor(5L); // Вот для этого нужен конструктор с id
        // Текущая дата и время
        LocalDateTime currentDateTime = LocalDateTime.now();
        //или
        LocalDateTime specificDateTime = LocalDateTime.of(2024, Month.MARCH, 15, 12, 30);
        //Добавление новой записи
        MedicalRecord medicalRecord = new MedicalRecord(13L, patient, doctor, "Головокружение", "Витамины", specificDateTime);
        //medicalRecordsRepository.save(medicalRecord);

        //Обновление новой записи
        medicalRecord.setDiagnosis("Мигрень.");
        //medicalRecordsRepository.update(medicalRecord);

        //Удаление записи
       // medicalRecordsRepository.delete(13L);

         System.out.println(" П А Ц И Е Н Т Ы");
        //Список всех пациентов
        System.out.println("Все пациенты:");
        patientRepository.findAll().forEach(System.out::println);

        // Поиск пациента по имени
        System.out.println("Пациенты с именем Иван:");
        System.out.println(patientRepository.findAllByFirstName("Иван"));

        //Поиск пациента по id
        System.out.println("Пациент по id: ");
        System.out.println(patientRepository.find(7L));

        // Создание нового пациента. Осторожно, создаёт!
        Patient newPatient = new Patient(12L, "Иван", "Иванчaйников", LocalDate.of(2000, 10, 3) , "8455678901", "ivanchein@example.com", "Москва");
        //patientRepository.save(newPatient);

        // Обновление нового пациента
        newPatient.setFirstName("Алексей");
        //patientRepository.update(newPatient);

        // Удаление пациента
        // Осторожно! Это работает!
        //patientRepository.delete(13L);

        // Вызываем метод updatePatient, получаем обновленный объект Patient
        Patient updatedPatient = patientRepository.updatePatient(11L, "Мария", "Иванчайникова");
        // Теперь можно работать с обновленным объектом
        System.out.println("Обновленный пациент: " + updatedPatient.getFirstName() + " " + updatedPatient.getLastName());

        System.out.println(" В Р А Ч И");
        //Список всех докторов
        System.out.println("Все врачи: ");
        doctorRepository.findAll().forEach(System.out::println);

        //Поиск доктора по id
        System.out.println("Доктор по id: ");
        System.out.println(doctorRepository.find(4L));

        // Поиск доктора по имени
        System.out.println("Врачи с именем Михаил:");
        System.out.println(doctorRepository.findAllByFirstName("Михаил"));

        // Создание нового доктора. Осторожно, создаёт!
        Doctor newDoctor = new Doctor(11L, "Юрий", "Павлов", "Вирусолог" , "9995678922", "jurapav@example.com", "11");
        //doctorRepository.save(newDoctor);

        // Обновление нового доктора
        newDoctor.setOfficeNumber("111");
        //doctorRepository.update(newDoctor);

        // Удаление доктора
        // Осторожно! Это работает!
       // doctorRepository.delete(11L);
    }
}
