package ru.natali.clinic.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.natali.clinic.model.Appointment;
import ru.natali.clinic.model.Doctor;
import ru.natali.clinic.model.Medservice;
import ru.natali.clinic.model.Patient;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
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
        return jdbcTemplate.query(SQL_APPOINTMENT_BY_STATUS, new Object[] {status}, new AppointmentRowMapper());
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
}
