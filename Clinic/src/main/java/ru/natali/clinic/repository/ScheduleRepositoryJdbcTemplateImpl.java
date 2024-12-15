package ru.natali.clinic.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.natali.clinic.model.Doctor;
import ru.natali.clinic.model.Schedule;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ScheduleRepositoryJdbcTemplateImpl implements ScheduleRepository {

    private final String SQL_SELECT_ALL_SCHEDULES = """
            SELECT * FROM schedules
            """;
    private final String SQL_SCHEDULE_BY_ID = """
            SELECT * FROM schedules WHERE id = ?
            """;
    private final String SQL_SCHEDULE_BY_DAY_OF_WEEK = """
            SELECT * FROM schedules WHERE day_of_week = ?
            """;
    private final String SQL_INSERT_SCHEDULE = """
            INSERT INTO schedules (doctor_id, day_of_week, start_time, end_time)VALUES (?, ?, ?, ?)
            """;
    private final String SQL_UPDATE_SCHEDULE = """
            UPDATE schedules SET doctor_id = ?, day_of_week = ?, start_time = ?, end_time = ? WHERE id = ?
            """;
    private final String SQL_DELETE_SCHEDULE_BY_ID = """
            DELETE FROM schedules WHERE id = ?
            """;

    private JdbcTemplate jdbcTemplate;
    public ScheduleRepositoryJdbcTemplateImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static class ScheduleRowMapper implements RowMapper<Schedule> {
        @Override
        public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
            Schedule schedule = new Schedule();
            schedule.setId(rs.getLong("id"));
            schedule.setDoctor(new Doctor(rs.getLong("doctor_id")));
            schedule.setDayOfWeek(rs.getString("day_of_week"));
            schedule.setStartTime(rs.getTime("start_time").toLocalTime());
            schedule.setEndTime(rs.getTime("end_time").toLocalTime());
            return schedule;
        }
    }

    @Override
    public List<Schedule> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL_SCHEDULES, new ScheduleRowMapper());
    }

    @Override
    public Optional<Schedule> find(Long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_SCHEDULE_BY_ID, new Object[]{id}, new ScheduleRowMapper()));
    }

    @Override
    public void save(Schedule schedule) {
        jdbcTemplate.update(
                SQL_INSERT_SCHEDULE,
                schedule.getDoctor().getId(),
                schedule.getDayOfWeek(),
                schedule.getStartTime(),
                schedule.getEndTime()
        );
    }

    @Override
    public void update(Schedule schedule) {
        jdbcTemplate.update(
                SQL_UPDATE_SCHEDULE,
                schedule.getDoctor().getId(),
                schedule.getDayOfWeek(),
                schedule.getStartTime(),
                schedule.getEndTime(),
                schedule.getId()
        );
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(SQL_DELETE_SCHEDULE_BY_ID, id);
    }

    @Override
    public List<Schedule> findAllByDayOfWeek(String dayOfWeek) {
        return jdbcTemplate.query(SQL_SCHEDULE_BY_DAY_OF_WEEK, new Object[] {dayOfWeek}, new ScheduleRowMapper());
    }
}
