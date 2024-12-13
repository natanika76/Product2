package ru.natali.clinic.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.natali.clinic.model.Patient;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PatientRepositoryJdbcTemplateImpl implements PatientRepository {

    private final String SQL_SELECT_ALL_PATIENTS = "SELECT * FROM patients";
    private final String SQL_SELECT_PATIENT_BY_FIRST_NAME = "SELECT * FROM patients WHERE first_name = ?";
    private final String SQL_SELECT_PATIENT_BY_ID = "SELECT * FROM patients WHERE id = ?";
    private final String SQL_INSERT_PATIENT = "INSERT INTO patients (first_name, last_name, birth_date, phone_number, email, address) VALUES (?, ?, ?, ?, ?, ?)";
    private final String SQL_UPDATE_PATIENT = "UPDATE patients SET first_name = ?, last_name = ?, birth_date = ?, phone_number = ?, email = ?, address = ? WHERE id = ?";
    private final String SQL_DELETE_PATIENT = "DELETE FROM patients WHERE id = ?";

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
        return jdbcTemplate.query(SQL_SELECT_PATIENT_BY_FIRST_NAME, new Object[] {firstName}, new PatientRowMapper());
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
}
