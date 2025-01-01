package ru.natali.clinic.oldfiles.attestation1;

import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.natali.clinic.model.*;
import ru.natali.clinic.repository.*;
import ru.natali.clinic.repository.impl.*;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

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
        //Последнему пациенту Полина Морозова поставить сегодняшнее число
        System.out.println("Пациенты, рожденные сегодня:");
        List<Patient> patientsBornToday = patientRepository.findPatientsBornToday();
        patientsBornToday.forEach(patient -> System.out.println(patient.getFirstName() + " " + patient.getLastName() + " " + patient.getBirthDate()));

        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Пациенты без медицинских записей:");
        List<Patient> patientsWithoutMedicalRecords = patientRepository.findPatientsWithoutMedicalRecords();
        patientsWithoutMedicalRecords.forEach(patient -> System.out.println(patient));

        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Получить список пациентов, которые пропустили свои последние встречи: ");
        List<PatientWithMissedAppointment> patientsWithMissedAppointments = appointmentRepository.getPatientsWithMissedAppointments();

        for (PatientWithMissedAppointment patient : patientsWithMissedAppointments) {
            System.out.println(patient.firstName() + " " + patient.lastName() + " " + patient.lastAppointmentDate());
        }

        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Найти самых загруженных врачей (по количеству назначений): ");
        List<DoctorWithAppointmentCount> busyDoctors = doctorRepository.getBusyDoctors();

        for (DoctorWithAppointmentCount doctor : busyDoctors) {
            System.out.println(doctor.firstName() + " " + doctor.lastName() + ": " + doctor.appointmentCount());
        }

        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Найти врачей, у которых больше всего пациентов за последний месяц: ");
        List<DoctorWithPatientCount> doctors2 = doctorRepository.getDoctorsWithCountsPatientsByMonth();

        for (DoctorWithPatientCount doctor : doctors2) {
            System.out.println(doctor.firstName() + " " + doctor.lastName() + " " + doctor.patientCount());
        }

        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Найти пациентов, у которых имя начинается с буквы А: " + "\r\n");
        String startingLetter = "А"; // Пример буквы для поиска
        List<Patient> patients = patientRepository.findPatientsByFirstNameStartingWith(startingLetter);

        for (Patient patient : patients) {
            System.out.println(patient.getFirstName() + " " + patient.getLastName());
        }

        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Отсортировать врачей по фамилии по алфавиту: " + "\r\n");
        List<Doctor> doctors = doctorRepository.findDoctorsSortedByLastName();
        for (Doctor doctor : doctors) {
            System.out.println(doctor.getFirstName() + " " + doctor.getLastName() + " " + doctor.getSpecialty());
        }

        System.out.println("Получить первые N записей о пациентах");
        List<Patient> patients2 = patientRepository.findTopNPatients(4);
        for (Patient patient : patients2) {
            System.out.println(patient.getFirstName() + " " + patient.getLastName() + " " + patient.getBirthDate());
        }

        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Подсчитать количество назначенных встреч для каждого врача: " + "\r\n");
        Map<Long, Integer> appointmentsPerDoctor = doctorRepository.countAppointmentsPerDoctor();

        appointmentsPerDoctor.forEach((doctorId, totalAppointments) ->
                System.out.println("Doctor ID: " + doctorId + ", Total Appointments: " + totalAppointments)
        );

        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Получить список встреч вместе с именами пациентов и врачей: " + "\r\n");
        List<AppointmentDetails> appointmentDetails = appointmentRepository.getAppointmentDetails();
        appointmentDetails.forEach(details -> {
            System.out.println("ID: " + details.getId());
            System.out.println("Пациент: " + details.getPatientFirstName() + " " + details.getPatientLastName());
            System.out.println("Доктор: " + details.getDoctorFirstName() + " " + details.getDoctorLastName());

            // Форматирование даты
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            String formattedDate = details.getAppointmentDate().format(formatter);
            System.out.println("Дата встречи: " + formattedDate);

            System.out.println("Статус: " + details.getStatus());
            System.out.println("---------------------");
        });

        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println("Найти все встречи, где пациент имеет запись в медицинской карте за последние 30 дней: " + "\r\n");
        List<Appointment> recentAppointments = appointmentRepository.findRecentMedicalRecordsAppointments();

        recentAppointments.forEach(appointment -> {
            System.out.println("Найдена встреча: " + appointment.getAppointmentDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) + " " + appointment.getStatus());
        });

        System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
        System.out.println(" Найти всех пациентов, родившихся после определенного года и имеющих определенный адрес " + "\r\n");
        LocalDate birthYear = LocalDate.now().minusYears(30);
        //LocalDate birthYear = LocalDate.of(1994, Month.JANUARY, 1);
        String address = "Липецк, Сиреневый пр., д.120 кв.50";
        List<Patient> patients3 = patientRepository.findPatientsByBirthYearAndAddress(birthYear, address);

        patients3.forEach(patient -> {
            System.out.println("Имя: " + patient.getFirstName());
            System.out.println("Фамилия: " + patient.getLastName());
            System.out.println("Адрес: " + patient.getAddress());
            System.out.println("Дата рождения: " + patient.getBirthDate());
            System.out.println("Телефон: " + patient.getPhoneNumber());
            System.out.println("Email: " + patient.getEmail());
            System.out.println("-------------------");
        });
    }
}
