package ru.natali.clinic.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.natali.clinic.model.Medservice;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MedserviceRepositoryJdbcTemplateImpl implements MedserviceRepository {

    private final String SQL_RECORD_BY_ID = """
            SELECT * FROM services WHERE id = ?
            """;
    private final String SQL_SELECT_ALL_SERVICES = """
            SELECT * FROM services
            """;
    private final String SQL_SERVICE_BY_NAME = """
            SELECT * FROM services WHERE name = ?
            """;
    private final String SQL_INSERT_SERVICE = """
            INSERT INTO services (name, description, price) VALUES (?, ?, ?)
            """;
    private final String SQL_UPDATE_SERVICE = """
            UPDATE services SET name = ?, description = ?, price = ? WHERE id = ?
            """;
    private final String SQL_DELETE_SERVICE_BY_ID = """
            DELETE FROM services WHERE id = ?
            """;

    private JdbcTemplate jdbcTemplate;
    public MedserviceRepositoryJdbcTemplateImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static class MedserviceRowMapper implements RowMapper<Medservice> {
        @Override
        public Medservice mapRow(ResultSet rs, int rowNum) throws SQLException {
           Medservice medservice = new Medservice();
            medservice.setId(rs.getLong("id"));
            medservice.setName(rs.getString("name"));
            medservice.setDescription(rs.getString("description"));
            medservice.setPrice(rs.getDouble("price"));
            return medservice;
        }
    }

    @Override
    public Optional<Medservice> find(Long id) {
        return Optional.ofNullable(jdbcTemplate.queryForObject(SQL_RECORD_BY_ID, new Object[]{id}, new MedserviceRowMapper()));
    }

    @Override
    public List<Medservice> findAll() {
        return jdbcTemplate.query(SQL_SELECT_ALL_SERVICES, new MedserviceRowMapper());
    }

    @Override
    public void save(Medservice model) {
        jdbcTemplate.update(
                SQL_INSERT_SERVICE,
                model.getName(),
                model.getDescription(),
                model.getPrice()
        );
    }

    @Override
    public void update(Medservice model) {
        jdbcTemplate.update(
                SQL_UPDATE_SERVICE,
                model.getName(),
                model.getDescription(),
                model.getPrice(),
                model.getId()
        );
    }

    @Override
    public void delete(Long id) {
        jdbcTemplate.update(SQL_DELETE_SERVICE_BY_ID, id);
    }

    @Override
    public Optional<Medservice> findByName(String name) {
        List<Medservice> result = jdbcTemplate.query(SQL_SERVICE_BY_NAME, new Object[] {name}, new MedserviceRowMapper());
        if (!result.isEmpty()) {
            return Optional.of(result.get(0));
        }
        return Optional.empty();
    }
}
