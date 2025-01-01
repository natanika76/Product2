package ru.natali.clinic.app;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.natali.clinic.exception.ClinicException;
import ru.natali.clinic.exception.doctorexception.DoctorNotFoundException;
import ru.natali.clinic.exception.doctorexception.DoctorSaveException;
import ru.natali.clinic.exception.doctorexception.DoctorUpdateException;
import ru.natali.clinic.exception.medserviceexception.*;
import ru.natali.clinic.model.*;
import ru.natali.clinic.repository.*;
import ru.natali.clinic.repository.impl.*;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MainClinic {
    public static void main(String[] args) {

        //Объект DataSource предоставляет Connection(интерфейс), который предоставляет доступ к БД
        DataSource dataSource = new DriverManagerDataSource("jdbc:postgresql://localhost:5433/healthcare",
                "postgres", "Lzmf2000");

        PatientRepository patientRepository = new PatientRepositoryJdbcTemplateImpl(dataSource);
        DoctorRepository doctorRepository = new DoctorRepositoryJdbcTemplateImpl(dataSource);
        MedicalRecordsRepository medicalRecordsRepository = new MedicalRecordsRepositoryJdbcTemplateImpl(dataSource);
        MedserviceRepository medserviceRepository = new MedserviceRepositoryJdbcTemplateImpl(dataSource);
        AppointmentRepository appointmentRepository = new AppointmentRepositoryJdbcTemplateImpl(dataSource);
        ScheduleRepository scheduleRepository = new ScheduleRepositoryJdbcTemplateImpl(dataSource);


        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println(" М Е Д У С Л У Г И");
        //Все медуслуги:
        System.out.println("Все виды медицинских услуг: ");
        medserviceRepository.findAll().forEach(System.out::println);

        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Тестирование поиска медицинской услуги по названию:");

        // 1. Успешный поиск медицинской услуги
        try {
            String existingName = "Консультация окулиста"; // Предположим, что услуга с этим именем существует
            Optional<Medservice> medservice = medserviceRepository.findByName(existingName);
            if (medservice.isPresent()) {
                System.out.println("Найдена медицинская услуга: " + medservice.get().getName());
            }
        } catch (MedserviceNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (MedserviceRepositoryException e) {
            System.out.println("Ошибка при поиске: " + e.getMessage());
        }

        // 2. Попытка найти несуществующую медицинскую услугу
        try {
            String nonExistingName = "Несуществующая услуга"; // Предположим, что услуга с этим именем не существует
            Optional<Medservice> medservice = medserviceRepository.findByName(nonExistingName);
            if (medservice.isPresent()) {
                System.out.println("Найдена медицинская услуга: " + medservice.get().getName());
            }
        } catch (MedserviceNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (MedserviceRepositoryException e) {
            System.out.println("Ошибка при поиске: " + e.getMessage());
        }
/*
          System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Удаление медицинской услуги по ID:");
        // 1. Успешное удаление медицинской услуги
        try {
            Long existingId = 7L;
            medserviceRepository.delete(existingId);
        } catch (MedserviceNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (MedserviceDeleteException e) {
            System.out.println("Ошибка при удалении: " + e.getMessage());
        }

        // 2. Попытка удалить несуществующую медицинскую услугу
        try {
            Long nonExistingId = 122L;
            medserviceRepository.delete(nonExistingId);
        } catch (MedserviceNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (MedserviceDeleteException e) {
            System.out.println("Ошибка при удалении: " + e.getMessage());
        }

    System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Тестирование обновления медицинской услуги по ID:");
        // 1. Успешное обновление медицинской услуги
        try {
            Long existingId = 3L;
            Medservice updatedMedservice = new Medservice();
            updatedMedservice.setName("Обновленная консультация");
            updatedMedservice.setDescription("Расширенная консультация терапевта");
            updatedMedservice.setPrice(2000.0);

            medserviceRepository.updateById(existingId, updatedMedservice);
        } catch (MedserviceNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (MedserviceUpdateException e) {
            System.out.println("Ошибка при обновлении: " + e.getMessage());
        }

        // 2. Попытка обновить несуществующую медицинскую услугу
        try {
            Long nonExistingId = 122L; // услуга с этим ID не существует
            Medservice updatedMedservice = new Medservice();
            updatedMedservice.setName("Несуществующая услуга");
            updatedMedservice.setDescription("Эта услуга не должна быть обновлена");
            updatedMedservice.setPrice(1000.0);

            medserviceRepository.updateById(nonExistingId, updatedMedservice);
        } catch (MedserviceNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (MedserviceUpdateException e) {
            System.out.println("Ошибка при обновлении: " + e.getMessage());
        }

        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Создание новой медицинской услуги: ");
        // 1. Успешное создание медицинской услуги
        try {
            Medservice medservice = new Medservice();
            medservice.setName("Консультация врача");
            medservice.setDescription("Первичная консультация терапевта");
            medservice.setPrice(1500.0);

            medserviceRepository.save(medservice);
            System.out.println("Медицинская услуга успешно создана: " + medservice.getName());
        } catch (MedserviceSaveException e) {
            System.out.println("Ошибка при создании медицинской услуги: " + e.getMessage());
        }

        // 2. Попытка создать медицинскую услугу с некорректными данными (null)
        try {
            Medservice invalidMedservice = new Medservice();
            invalidMedservice.setName(null);
            invalidMedservice.setDescription("Невалидная услуга");
            invalidMedservice.setPrice(-100.0);

            medserviceRepository.save(invalidMedservice);
            System.out.println("Медицинская услуга успешно создана: " + invalidMedservice.getName());
        } catch (MedserviceSaveException e) {
            System.out.println("Ошибка при создании медицинской услуги: " + e.getMessage());
        }


        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Удаление всех медицинских услуг: ");
        //medserviceRepository.deleteAll(); //дважды покажет Список медицинских услуг уже пуст. Удалять нечего.

        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Поиск медуслуги по id с кастомным исключением: ");
        try {
            Medservice medservice = medserviceRepository.findById(3L);
            System.out.println("Найдена медицинская услуга: " + medservice.getName());
        } catch (MedserviceNotFoundException e) {
            System.out.println(e.getMessage());
        }

        try {
            Medservice medservice = medserviceRepository.findById(122L);
            System.out.println("Найдена медицинская услуга: " + medservice.getName());
        } catch (MedserviceNotFoundException e) {
            System.out.println(e.getMessage());
        }
      System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Поиск медуслуги по id с кастомным исключением(Optional): ");

        Optional<Medservice> result1 = medserviceRepository.find(3L);
        if (result1.isPresent()) {
            System.out.println("Найдена медицинская услуга: " + result1.get().getName());
        } else {
            System.out.println("Медицинская услуга с ID 3 не найдена");
        }

        Optional<Medservice> result2 = medserviceRepository.find(122L);
        if (result2.isPresent()) {
            System.out.println("Найдена медицинская услуга: " + result2.get().getName());
        } else {
            System.out.println("Медицинская услуга с ID 122 не найдена");
        }*/
/*
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println(" П А Ц И Е Н Т Ы");
        //Список всех пациентов
        System.out.println("Все пациенты:");
        patientRepository.findAll().forEach(System.out::println);

        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Удаление всех пациентов: "); //скрин DeleteAllException.pdf
        //patientRepository.deleteAll();

        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Поиск пациента по id с кастомным исключением: "); //скрин FindById.pdf
        //patientRepository.find(133L);

        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Создание нового пациента: ");
        // Создаем нового пациента
        Patient patient = new Patient();
        patient.setFirstName("Иван");
        //patient.setFirstName(null); // Имя не может быть null скрин SaveCustomException.pdf
        patient.setLastName("Иванов");
        patient.setBirthDate(LocalDate.of(1990, 5, 15));
        patient.setPhoneNumber("+7-999-123-45-67");
        patient.setEmail("ivan.ivanov@example.com");
        patient.setAddress("ул. Ленина, д. 10");
*/
        /*try {
            // Сохраняем пациента
            patientRepository.save(patient);
            System.out.println("Пациент успешно сохранен!");
        } catch (ClinicException e) {
            System.err.println("Ошибка при сохранении пациента: " + e.getMessage());
            e.printStackTrace();
        }*/
/*
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Обновление нового пациента: ");
        try {
            // Получаем список всех пациентов
            List<Patient> patients = patientRepository.findAll();

            if (!patients.isEmpty()) {
                // Берем первого пациента из списка
                Patient existingPatient = patients.get(0);
                //Patient existingPatient = patients.get(122); //скрин NotFoundByIdException.pdf
                Long patientId = existingPatient.getId();
                System.out.println("Найден пациент для обновления: " + existingPatient);

                // Обновляем данные пациента
                Patient updatedPatient = new Patient();
                updatedPatient.setId(patientId); // Указываем тот же ID
                updatedPatient.setFirstName("НовоеИмя");
                //updatedPatient.setFirstName(null);
                updatedPatient.setLastName("НоваяФамилия");
                updatedPatient.setBirthDate(existingPatient.getBirthDate()); // Оставляем ту же дату рождения
                updatedPatient.setPhoneNumber("+7-999-000-00-00");
                updatedPatient.setEmail("new.email@example.com");
                updatedPatient.setAddress("Новый адрес");

                // Вызываем метод update
                patientRepository.update(updatedPatient);
                System.out.println("Данные пациента успешно обновлены!");

                // Проверяем, что данные обновились
                Optional<Patient> foundPatient = patientRepository.find(patientId);
                if (foundPatient.isPresent()) {
                    System.out.println("Обновленные данные пациента: " + foundPatient.get());
                } else {
                    System.out.println("Пациент с ID " + patientId + " не найден.");
                }
            } else {
                System.out.println("В базе данных нет пациентов.");
            }
        } catch (ClinicException e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }

*/
 /*
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println(" В Р А Ч И");
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Выгрузка всех врачей из базы: ");
        //Список всех докторов
        doctorRepository.findAll().forEach(System.out::println);

        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Поиск доктора по id с кастомным исключением: ");
        System.out.println("Доктор по id: ");
        System.out.println(doctorRepository.find(1L));
        System.out.println(doctorRepository.findById(2L));

        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Создание нового доктора: ");
        //Осторожно, создаёт!
        //Чтобы проверить кастомное исключение, вместо имени подставить null
        Doctor newDoctor = new Doctor(21L, "Юрий", "Павлов", "Вирусолог" , "9995678922", "jurapav@example.com", "11");
        //doctorRepository.save(newDoctor);

        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Обновление нового доктора: ");
        newDoctor.setOfficeNumber("111");
        //newDoctor.setLastName(null);
        //doctorRepository.update(newDoctor);

        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Удаление данных одного врача: ");
        // Осторожно! Это работает! Сам по себе запрос корректен, исключения не будет
        //Исправила delete - если количество обновленных строк равно 0, это означает, что запись не была найдена, и можно выбросить исключение
         //doctorRepository.delete(21L);

        //Без удаления всех записей не удаляются все врачи
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Удаление всех медицинских записей: ");
        //medicalRecordsRepository.deleteAll();

        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Удаление всех врачей: "); //прежде нужно удалить таблицу medical_records
        //doctorRepository.deleteAll();

        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Обновление конкретного врача по идентификатору: ");

        Doctor updatedDoctor = new Doctor();
        updatedDoctor.setFirstName("Новое Имя");
        updatedDoctor.setLastName("Новая Фамилия2");
        //doctor.setLastName(null); //для проверки кастомного исключения
        updatedDoctor.setSpecialty("Терапевт");
        updatedDoctor.setPhoneNumber("123-456-7890");
        updatedDoctor.setEmail("new_email@example.com");
        updatedDoctor.setOfficeNumber("202");*/
/*
        try {
            doctorRepository.update(133L, updatedDoctor); // Обновляем врача с ID существующим, если нет - создается новый врач
            System.out.println("Данные врача успешно обновлены.");
        } catch (DoctorUpdateException e) {
            System.err.println("Ошибка при обновлении данных врача: " + e.getMessage());
        } catch (DoctorSaveException e) {
            System.err.println("Ошибка при создании нового врача: " + e.getMessage());
        }
*/
        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Список врачей, отсортированных по фамилии: ");
        // Вызов метода и обработка исключения
        try {
            List<Doctor> doctors = doctorRepository.findDoctorsSortedByLastName();
            if (doctors.isEmpty()) {
                throw new DoctorNotFoundException("Врачи не найдены.");
            }
            System.out.println("Список врачей, отсортированных по фамилии:");
            for (Doctor doctor : doctors) {
                System.out.println(doctor);
            }
        } catch (DoctorNotFoundException e) {
            System.err.println("Ошибка: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ошибка при получении списка врачей: " + e.getMessage());
        }
    }
}

