package ru.natali.clinic.repository.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.natali.clinic.exception.medserviceexception.*;
import ru.natali.clinic.model.Medservice;
import ru.natali.clinic.repository.MedserviceRepository;

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
    private final String SQL_DELETE_ALL_SERVICES = """
            DELETE FROM services
            """;
    private final String SQL_SERVICES_IS_PRESENT = """
            SELECT COUNT(*) FROM services
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
    public Medservice findById(Long id) {
        try {
            return jdbcTemplate.queryForObject(SQL_RECORD_BY_ID, new Object[]{id}, new MedserviceRowMapper());
        } catch (EmptyResultDataAccessException e) {
            throw new MedserviceNotFoundException("Медицинская услуга с ID " + id + " не найдена");
        } catch (Exception e) {
            throw new MedserviceRepositoryException("Ошибка при поиске медицинской услуги по ID: " + id, e);
        }
    }

    @Override
    public List<Medservice> findAll() {
        try {
            return jdbcTemplate.query(SQL_SELECT_ALL_SERVICES, new MedserviceRowMapper());
        } catch (Exception e) {
            throw new MedserviceRepositoryException("Ошибка при получении списка медицинских услуг", e);
        }
    }

    @Override
    public void deleteAll() {
        try {
            // Проверяем, есть ли записи в таблице
            int count = jdbcTemplate.queryForObject(SQL_SERVICES_IS_PRESENT, Integer.class);
            if (count == 0) {
                System.out.println("Список медицинских услуг уже пуст. Удалять нечего.");
            } else {
                // Если записи есть, выполняем удаление
                jdbcTemplate.update(SQL_DELETE_ALL_SERVICES);
                System.out.println("Все медицинские услуги успешно удалены.");
            }
        } catch (Exception e) {
            // Если произошла ошибка, выбрасываем кастомное исключение
            throw new MedserviceDeleteException("Не удалось удалить все медицинские услуги", e);
        }
    }

    @Override
    public Optional<Medservice> find(Long id) {
        try {
            Medservice medservice = jdbcTemplate.queryForObject(SQL_RECORD_BY_ID, new Object[]{id}, new MedserviceRowMapper());
            return Optional.ofNullable(medservice);
        } catch (EmptyResultDataAccessException e) {
            // Если запись не найдена, возвращаем Optional.empty()
            return Optional.empty();
        } catch (Exception e) {
            // Если произошла другая ошибка, выбрасываем кастомное исключение
            throw new MedserviceRepositoryException("Ошибка при поиске медицинской услуги по ID: " + id, e);
        }
    }

    @Override
    public void save(Medservice model) {
        try {
            jdbcTemplate.update(
                    SQL_INSERT_SERVICE,
                    model.getName(),
                    model.getDescription(),
                    model.getPrice()
            );
        } catch (Exception e) {
            throw new MedserviceSaveException("Не удалось сохранить медицинскую услугу", e);
        }
    }

    @Override
    public void update(Medservice model) {
        try {
            jdbcTemplate.update(
                    SQL_UPDATE_SERVICE,
                    model.getName(),
                    model.getDescription(),
                    model.getPrice(),
                    model.getId()
            );
        } catch (Exception e) {
            throw new MedserviceUpdateException("Не удалось обновить медицинскую услугу", e);
        }
    }

    @Override
    public void updateById(Long id, Medservice updatedMedservice) {
        try {
            // Проверяем, существует ли услуга с указанным id
            Optional<Medservice> existingMedservice = find(id);
            if (existingMedservice.isEmpty()) {
                throw new MedserviceNotFoundException("Медицинская услуга с ID " + id + " не найдена");
            }

            // Обновляем данные услуги
            jdbcTemplate.update(
                    SQL_UPDATE_SERVICE,
                    updatedMedservice.getName(),
                    updatedMedservice.getDescription(),
                    updatedMedservice.getPrice(),
                    id
            );
            System.out.println("Медицинская услуга с ID " + id + " успешно обновлена.");
        } catch (MedserviceNotFoundException e) {
            // Перехватываем кастомное исключение и выбрасываем его
            throw e;
        } catch (Exception e) {
            // Если произошла другая ошибка, выбрасываем кастомное исключение
            throw new MedserviceUpdateException("Не удалось обновить медицинскую услугу с ID " + id, e);
        }
    }
    @Override
    public void delete(Long id) {
        try {
            // Проверяем, существует ли услуга с указанным id
            Optional<Medservice> existingMedservice = find(id);
            if (existingMedservice.isEmpty()) {
                throw new MedserviceNotFoundException("Медицинская услуга с ID " + id + " не найдена");
            }

            // Если услуга существует, выполняем удаление
            jdbcTemplate.update(SQL_DELETE_SERVICE_BY_ID, id);
            System.out.println("Медицинская услуга с ID " + id + " успешно удалена.");
        } catch (MedserviceNotFoundException e) {
            // Перехватываем кастомное исключение и выбрасываем его
            throw e;
        } catch (Exception e) {
            // Если произошла другая ошибка, выбрасываем кастомное исключение
            throw new MedserviceDeleteException("Не удалось удалить медицинскую услугу с ID " + id, e);
        }
    }

    @Override
    public Optional<Medservice> findByName(String name) {
        try {
            // Выполняем поиск по имени
            List<Medservice> result = jdbcTemplate.query(SQL_SERVICE_BY_NAME, new Object[]{name}, new MedserviceRowMapper());
            if (result.isEmpty()) {
                // Если услуга не найдена, выбрасываем исключение
                throw new MedserviceNotFoundException("Медицинская услуга с именем '" + name + "' не найдена");
            }
            // Возвращаем первую найденную услугу (если их несколько)
            return Optional.of(result.get(0));
        } catch (MedserviceNotFoundException e) {
            // Перехватываем кастомное исключение и выбрасываем его
            throw e;
        } catch (Exception e) {
            // Если произошла другая ошибка, выбрасываем кастомное исключение
            throw new MedserviceRepositoryException("Ошибка при поиске медицинской услуги по имени: " + name, e);
        }
    }
}
