package ru.natali.clinic.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.natali.clinic.model.Doctor;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class DoctorRepositoryJdbcTemplateImpl implements DoctorRepository {

    private final String SQL_SELECT_DOCTOR_BY_ID = """
            SELECT * FROM doctors WHERE id = ?
            """;
    private final String SQL_SELECT_ALL_DOCTORS = """
            SELECT * FROM doctors
            """;
    private final String SQL_INSERT_DOCTOR = """
            INSERT INTO doctors (first_name, last_name, specialty, phone_number, email, office_number) VALUES (?, ?, ?, ?, ?, ?)
            """;
    private final String SQL_UPDATE_DOCTOR = """
            UPDATE doctors SET first_name = ?, last_name = ?, specialty = ?, phone_number = ?, email = ?, office_number = ? WHERE id = ?
            """;
    private final String SQL_DELETE_DOCTOR_BY_ID = """
            DELETE FROM doctors WHERE id = ?
            """;
    private final String SQL_SELECT_DOCTOR_BY_FIRST_NAME = """
             SELECT * FROM doctors WHERE first_name = ?
            """;

    private final JdbcTemplate jdbcTemplate;

    public DoctorRepositoryJdbcTemplateImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static class DoctorRowMapper implements RowMapper<Doctor> {
        @Override
        public Doctor mapRow(ResultSet rs, int rowNum) throws SQLException {
            Doctor doctor = new Doctor();
            doctor.setId(rs.getLong("id"));
            doctor.setFirstName(rs.getString("first_name"));
            doctor.setLastName(rs.getString("last_name"));
            doctor.setSpecialty(rs.getString("specialty"));
            doctor.setPhoneNumber(rs.getString("phone_number"));
            doctor.setEmail(rs.getString("email"));
            doctor.setOfficeNumber(rs.getString("office_number"));
            return doctor;
        }
    }

    @Override
    public Optional<Doctor> find(Long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_SELECT_DOCTOR_BY_ID, new Object[]{id}, new DoctorRowMapper()));
    }

    @Override
    public void save(Doctor doctor) {
        jdbcTemplate.update(
                SQL_INSERT_DOCTOR,
                doctor.getFirstName(),
                doctor.getLastName(),
                doctor.getSpecialty(),
                doctor.getPhoneNumber(),
                doctor.getEmail(),
                doctor.getOfficeNumber()
        );
    }

    @Override
    public void update(Doctor doctor) {
        jdbcTemplate.update(
                SQL_UPDATE_DOCTOR,
                doctor.getFirstName(),
                doctor.getLastName(),
                doctor.getSpecialty(),
                doctor.getPhoneNumber(),
                doctor.getEmail(),
                doctor.getOfficeNumber(),
                doctor.getId()
        );
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(SQL_DELETE_DOCTOR_BY_ID, id);
    }

    @Override
    public List<Doctor> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL_DOCTORS, new DoctorRowMapper());
    }

    @Override
    public List<Doctor> findAllByFirstName(String firstName) {
        return jdbcTemplate.query(SQL_SELECT_DOCTOR_BY_FIRST_NAME, new Object[] {firstName}, new DoctorRowMapper());
    }

}
