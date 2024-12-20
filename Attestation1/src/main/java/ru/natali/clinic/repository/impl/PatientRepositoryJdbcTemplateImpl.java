package ru.natali.clinic.repository.impl;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.natali.clinic.model.Patient;
import ru.natali.clinic.repository.PatientRepository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class PatientRepositoryJdbcTemplateImpl implements PatientRepository {

    private final String SQL_SELECT_ALL_PATIENTS = """
                SELECT * FROM patients
            """;
    private final String SQL_SELECT_PATIENT_BY_FIRST_NAME = """
                SELECT * FROM patients WHERE first_name = ?
            """;
    private final String SQL_SELECT_PATIENT_BY_ID = """
                SELECT * FROM patients WHERE id = ?
            """;
    private final String SQL_INSERT_PATIENT = """
                INSERT INTO patients (first_name, last_name, birth_date, phone_number, email, address) VALUES (?, ?, ?, ?, ?, ?)
            """;
    private final String SQL_UPDATE_PATIENT = """
            UPDATE patients SET first_name = ?, last_name = ?, birth_date = ?, phone_number = ?, email = ?, address = ? WHERE id = ?
            """;
    private final String SQL_DELETE_PATIENT = """
                DELETE FROM patients WHERE id = ?
            """;
    private final String SQL_ALL_PATIENTS_OF_DOCTOR = """
                    SELECT p.id, p.first_name, p.last_name
                    FROM medical_records mr
                    JOIN patients p ON mr.patient_id = p.id
                    WHERE mr.doctor_id = ?
            """;
    private final String SQL_FIND_PATIENT_STARTING_WITH = """
            SELECT * FROM patients WHERE first_name LIKE ?
            """;

    private final String SQL_LIMIT_PATIENTS = """
            SELECT * FROM patients LIMIT ?
            """;

    private final String SQL_PATIENTS_BY_DATE_AND_ADDRESS = """
           SELECT * FROM patients WHERE birth_date >= ? AND address = ?
           """;
    private final String SQL_PATIENTS_WITHOUT_RECORDS =
            """
           WITH diagnosed_patients AS (
               SELECT DISTINCT m.patient_id
               FROM medical_records m
           )
           SELECT p.id, p.first_name, p.last_name
           FROM patients p
           WHERE p.id NOT IN (SELECT dp.patient_id FROM diagnosed_patients dp);
           """;
    private final String SQL_PATIENTS_BIRTHDAY_TODAY =
            """
                    SELECT *
                    FROM patients
                    WHERE EXTRACT(MONTH FROM birth_date) = EXTRACT(MONTH FROM CURRENT_DATE)
                      AND EXTRACT(DAY FROM birth_date) = EXTRACT(DAY FROM CURRENT_DATE);
           """;

    // Объект JdbcTemplate для выполнения запросов
    private final JdbcTemplate jdbcTemplate;

    public PatientRepositoryJdbcTemplateImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // Внутренний класс для отображения строки результата в объект Patient
    // Можно сделать его final, но он и так достаточно инкапсулирован
    private static class PatientRowMapper implements RowMapper<Patient> {
        @Override
        public Patient mapRow(ResultSet rs, int rowNum) throws SQLException {
            Patient patient = new Patient();
            patient.setId(rs.getLong("id"));
            patient.setFirstName(rs.getString("first_name"));
            patient.setLastName(rs.getString("last_name"));
            patient.setBirthDate(rs.getDate("birth_date").toLocalDate());
            patient.setPhoneNumber(rs.getString("phone_number"));
            patient.setEmail(rs.getString("email"));
            patient.setAddress(rs.getString("address"));
            return patient;
        }
    }

    @Override
    public List<Patient> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL_PATIENTS, new PatientRowMapper());
    }

    @Override
    public List<Patient> findAllByFirstName(String firstName) {
        return jdbcTemplate.query(SQL_SELECT_PATIENT_BY_FIRST_NAME, new Object[]{firstName}, new PatientRowMapper());
    }

    @Override
    public Optional<Patient> find(Long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_SELECT_PATIENT_BY_ID, new Object[]{id}, new PatientRowMapper()));
    }

    @Override
    public void save(Patient patient) {
        jdbcTemplate.update(
                SQL_INSERT_PATIENT,
                patient.getFirstName(),
                patient.getLastName(),
                patient.getBirthDate(),
                patient.getPhoneNumber(),
                patient.getEmail(),
                patient.getAddress()
        );
    }

    @Override
    public void update(Patient patient) {
        jdbcTemplate.update(
                SQL_UPDATE_PATIENT,
                patient.getFirstName(),
                patient.getLastName(),
                patient.getBirthDate(),
                patient.getPhoneNumber(),
                patient.getEmail(),
                patient.getAddress(),
                patient.getId()
        );
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(SQL_DELETE_PATIENT, id);
    }

    @Override
    public Patient updatePatient(Long id, String newFirstName, String newLastName) {
        Optional<Patient> optionalPatient = find(id);

        if (optionalPatient.isPresent()) {
            Patient patient = optionalPatient.get();

            patient.setFirstName(newFirstName);
            patient.setLastName(newLastName);

            update(patient);

            return patient; // Возвращаем обновленного пациента
        } else {
            throw new RuntimeException("Пациент с id=" + id + " не найден");
        }
    }

    @Override
    public Optional<List<Patient>> findPatientsByDoctorId(long doctorId) {
        List<Patient> patients = jdbcTemplate.query(
                SQL_ALL_PATIENTS_OF_DOCTOR,
                new Object[]{doctorId},
                (rs, rowNum) -> new Patient(rs.getLong("id"), rs.getString("first_name"), rs.getString("last_name"))
        );

        return Optional.ofNullable(patients.isEmpty() ? null : patients);
    }

    @Override
    public List<Patient> findPatientsByFirstNameStartingWith(String letter) {
        return jdbcTemplate.query(SQL_FIND_PATIENT_STARTING_WITH , new Object[] {letter + "%"}, new BeanPropertyRowMapper<>(Patient.class));
    }

    @Override
    public List<Patient> findTopNPatients(int n) {
        return jdbcTemplate.query(
                SQL_LIMIT_PATIENTS,
                new Object[] {n},
                new BeanPropertyRowMapper<>(Patient.class)
        );
    }

    @Override
    public List<Patient> findPatientsByBirthYearAndAddress(LocalDate birthYear, String address) {
        return jdbcTemplate.query(
                SQL_PATIENTS_BY_DATE_AND_ADDRESS,
                new Object[] {birthYear.atStartOfDay(), address},
                new BeanPropertyRowMapper<>(Patient.class)
        );

    }

    @Override
    public List<Patient> findPatientsWithoutMedicalRecords() {
        return jdbcTemplate.query(SQL_PATIENTS_WITHOUT_RECORDS, (rs, rowNum) ->
                new Patient(
                        rs.getLong("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name")
                ));
    }

    @Override
    public List<Patient> findPatientsBornToday() {
        return jdbcTemplate.query(SQL_PATIENTS_BIRTHDAY_TODAY, (rs, rowNum) ->
                new Patient(
                        rs.getLong("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getObject("birth_date", LocalDate.class),
                        rs.getString("phone_number"),
                        rs.getString("email"),
                        rs.getString("address")
                ));
    }
}
