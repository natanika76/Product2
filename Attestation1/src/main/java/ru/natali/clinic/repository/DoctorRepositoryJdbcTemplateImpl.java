package ru.natali.clinic.repository;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.natali.clinic.model.Doctor;
import ru.natali.clinic.model.DoctorWithAppointmentCount;
import ru.natali.clinic.model.DoctorWithPatientCount;

import java.util.stream.Stream;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final String SQL_SORT_DOCTOR_BY_LAST_NAME = """
            SELECT * FROM doctors ORDER BY last_name ASC
            """;
    private final String SQL_TOTAL_APPOINTMENTS_FOR_DOCTOR = """
            SELECT doctor_id, COUNT(*) AS total_appointments FROM appointments GROUP BY doctor_id
            """;
    private final String SQL_DOCTORS_WITH_COUNTS_PATIENTS_BY_MONTH = """
            WITH recent_patients AS (
                SELECT doctor_id, COUNT(*) as patient_count
                FROM appointments
                WHERE appointment_date >= NOW() - INTERVAL '1 month'
                GROUP BY doctor_id
            )
            SELECT d.id, d.first_name, d.last_name, rp.patient_count
            FROM doctors d
                     JOIN recent_patients rp ON d.id = rp.doctor_id
            ORDER BY rp.patient_count DESC;
            """;
    private final String SQL_BUSY_DOCTORS = """
            WITH busy_doctors AS (
                SELECT doctor_id, COUNT(*) as appointment_count
                FROM appointments
                GROUP BY doctor_id
            )
            SELECT d.id, d.first_name, d.last_name, bd.appointment_count
            FROM doctors d
            JOIN busy_doctors bd ON d.id = bd.doctor_id
            ORDER BY bd.appointment_count DESC;
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

    @Override
    public List<Doctor> findDoctorsSortedByLastName() {
        return jdbcTemplate.query(
                SQL_SORT_DOCTOR_BY_LAST_NAME,
                new BeanPropertyRowMapper<>(Doctor.class)
        );
    }

    @Override
    public Map<Long, Integer> countAppointmentsPerDoctor() {
        return jdbcTemplate.query(
                        SQL_TOTAL_APPOINTMENTS_FOR_DOCTOR,
                        (resultSet, rowNum) -> {
                            long doctorId = resultSet.getLong("doctor_id");
                            int totalAppointments = resultSet.getInt("total_appointments");
                            return new AbstractMap.SimpleEntry<>(doctorId, totalAppointments);
                        }
                ).stream()
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    @Override
    public List<DoctorWithPatientCount> getDoctorsWithCountsPatientsByMonth() {
        return jdbcTemplate.query(SQL_DOCTORS_WITH_COUNTS_PATIENTS_BY_MONTH,
                (rs, rowNum) -> new DoctorWithPatientCount(
                        rs.getLong("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("patient_count")
                ));
    }

    @Override
    public List<DoctorWithAppointmentCount> getBusyDoctors() {
        return jdbcTemplate.query(SQL_BUSY_DOCTORS,
                (rs, rowNum) -> new DoctorWithAppointmentCount(
                        rs.getLong("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getInt("appointment_count")
                ));
    }

}
