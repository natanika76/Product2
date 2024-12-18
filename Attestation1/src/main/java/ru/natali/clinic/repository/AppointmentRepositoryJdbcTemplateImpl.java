package ru.natali.clinic.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.natali.clinic.model.*;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public class AppointmentRepositoryJdbcTemplateImpl implements AppointmentRepository {

    private final String SQL_APPOINTMENT_BY_ID = """
            SELECT * FROM appointments WHERE id = ?
            """;
    private final String SQL_SELECT_ALL_APPOINTMENTS = """
            SELECT * FROM appointments
            """;
    private final String SQL_APPOINTMENT_BY_STATUS = """
            SELECT * FROM appointments WHERE status = ?
            """;
    private final String SQL_INSERT_APPOINTMENT = """
            INSERT INTO appointments (patient_id, doctor_id, service_id, appointment_date, status) VALUES (?, ?, ?, ?, ?)
            """;
    private final String SQL_UPDATE_APPOINTMENT = """
            UPDATE appointments SET patient_id = ?, doctor_id = ?, service_id = ?, appointment_date = ?, status = ? WHERE id = ?
            """;
    private final String SQL_DELETE_APPOINTMENT_BY_ID = """
            DELETE FROM appointments WHERE id = ?
            """;
    private final String SQL_APPOINTMENTS_DETAILS = """
            SELECT a.id, p.first_name as patient_first_name, p.last_name as patient_last_name, d.first_name as doctor_first_name, d.last_name as doctor_last_name, a.appointment_date, a.status
                                    FROM appointments a JOIN patients p ON a.patient_id = p.id
                                    JOIN doctors d ON a.doctor_id = d.id
            """;
    private final String SQL_APPOINTMENTS_LAST_MONTH = """
            SELECT a.* FROM appointments a WHERE a.patient_id IN (SELECT m.patient_id FROM medical_records m WHERE m.record_date > CURRENT_DATE - INTERVAL '30' DAY)
            """;
    private final String SQL_MISSED_APPOINTMENTS = """
            WITH missed_appointments AS (
                SELECT a.patient_id, MAX(a.appointment_date) as last_appointment_date
                FROM appointments a
                WHERE a.status = 'Отменен'
                GROUP BY a.patient_id
            )
            SELECT p.id, p.first_name, p.last_name, ma.last_appointment_date
            FROM patients p
                     JOIN missed_appointments ma ON p.id = ma.patient_id;
            """;
    //именно этот класс нам нужен от библиотеки spring-jdbc
    private JdbcTemplate jdbcTemplate;

    public AppointmentRepositoryJdbcTemplateImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static class AppointmentRowMapper implements RowMapper<Appointment> {
        //описываем правило, по которому строка ResultSet-a преобразуется в объект
        @Override
        public Appointment mapRow(ResultSet rs, int rowNum) throws SQLException {
            Appointment appointment = new Appointment();
            appointment.setId(rs.getLong("id"));
            appointment.setPatient(new Patient(rs.getLong("patient_id")));
            appointment.setDoctor(new Doctor(rs.getLong("doctor_id")));
            appointment.setService(new Medservice(rs.getLong("service_id")));
            appointment.setAppointmentDate(rs.getTimestamp("appointment_date").toLocalDateTime());
            appointment.setStatus(rs.getString("status"));
            return appointment;
        }
    }

    @Override
    public Optional<Appointment> find(Long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_APPOINTMENT_BY_ID, new Object[]{id}, new AppointmentRowMapper()));
    }

    @Override
    public List<Appointment> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL_APPOINTMENTS, new AppointmentRowMapper());
    }

    @Override
    public List<Appointment> findByStatus(String status) {
        return jdbcTemplate.query(SQL_APPOINTMENT_BY_STATUS, new Object[]{status}, new AppointmentRowMapper());
    }

    @Override
    public void save(Appointment appointment) {
        jdbcTemplate.update(
                SQL_INSERT_APPOINTMENT,
                appointment.getPatient().getId(),
                appointment.getDoctor().getId(),
                appointment.getService().getId(),
                appointment.getAppointmentDate(),
                appointment.getStatus()
        );
    }

    @Override
    public void update(Appointment appointment) {
        jdbcTemplate.update(
                SQL_UPDATE_APPOINTMENT,
                appointment.getPatient().getId(),
                appointment.getDoctor().getId(),
                appointment.getService().getId(),
                appointment.getAppointmentDate(),
                appointment.getStatus(),
                appointment.getId()
        );
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(SQL_DELETE_APPOINTMENT_BY_ID, id);
    }

    @Override
    public List<AppointmentDetails> getAppointmentDetails() {
        return jdbcTemplate.query(
                SQL_APPOINTMENTS_DETAILS,
                new RowMapper<AppointmentDetails>() {
                    @Override
                    public AppointmentDetails mapRow(ResultSet rs, int rowNum) throws SQLException {
                        AppointmentDetails details = new AppointmentDetails();
                        details.setId(rs.getLong("id"));
                        details.setPatientFirstName(rs.getString("patient_first_name"));
                        details.setPatientLastName(rs.getString("patient_last_name"));
                        details.setDoctorFirstName(rs.getString("doctor_first_name"));
                        details.setDoctorLastName(rs.getString("doctor_last_name"));

                        // Получаем Timestamp и преобразуем его в LocalDateTime
                        LocalDateTime appointmentDate = rs.getTimestamp("appointment_date").toLocalDateTime();

                        // Устанавливаем значение в объект AppointmentDetails
                        details.setAppointmentDate(appointmentDate);
                        details.setStatus(rs.getString("status"));
                        return details;
                    }
                }
        );
    }

    public List<Appointment> findRecentMedicalRecordsAppointments() {
        return jdbcTemplate.query(
                SQL_APPOINTMENTS_LAST_MONTH,
        new BeanPropertyRowMapper<>(Appointment.class)
        );
    }

    @Override
    public List<PatientWithMissedAppointment> getPatientsWithMissedAppointments() {
        return jdbcTemplate.query(SQL_MISSED_APPOINTMENTS,
                (rs, rowNum) -> new PatientWithMissedAppointment(
                        rs.getLong("id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("last_appointment_date").toLocalDate()
                ));
    }

}

