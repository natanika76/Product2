package ru.natali.students.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.natali.students.model.Student;

import javax.sql.DataSource;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class StudentRepositoryImpl implements StudentRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public StudentRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    // Получение списка всех студентов
    @Override
    public List<Student> findAll() {
        return jdbcTemplate.query("SELECT * FROM students", new StudentRowMapper());
    }

    // Поиск студента по ID
    @Override
    public Optional<Student> findById(Long id) {
        try {
            List<Student> results = jdbcTemplate.query("SELECT * FROM students WHERE id = ?", new StudentRowMapper(), id);
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } catch (DataAccessException e) {
            throw new RuntimeException("Ошибка поиска студента по id в базе данных.", e);
        }
    }

    @Override
    public Student save(Student student) {
        try {
            Connection conn = jdbcTemplate.getDataSource().getConnection();
            Array coursesArray = conn.createArrayOf("TEXT", student.getCourses().toArray());

            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("fullName", student.getFullName())
                    .addValue("email", student.getEmail())
                    .addValue("courses", coursesArray);

            NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
            namedJdbcTemplate.update("INSERT INTO students (full_name, email, courses) VALUES (:fullName, :email, :courses)", params);

            conn.close(); // Закрываем соединение после завершения работы
            return student;
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException("Ошибка сохранения студента в базу данных.", e);
        }
    }

    @Override
    public int update(Student student) {
        try {
            Connection conn = jdbcTemplate.getDataSource().getConnection();
            Array coursesArray = conn.createArrayOf("TEXT", student.getCourses().toArray());

            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("fullName", student.getFullName())
                    .addValue("email", student.getEmail())
                    .addValue("courses", coursesArray)
                    .addValue("id", student.getId());

            NamedParameterJdbcTemplate namedJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
            int rowsAffected = namedJdbcTemplate.update("UPDATE students SET full_name = :fullName, email = :email, courses = :courses WHERE id = :id", params);

            conn.close(); // Закрываем соединение после завершения работы
            return rowsAffected;
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException("Ошибка обновления информации о студенте в базе данных.", e);
        }
    }

    // Удаление студента по ID
    @Override
    public void deleteById(Long id) {
        try {
            jdbcTemplate.update("DELETE FROM students WHERE id = ?", id);
        } catch (DataAccessException e) {
            throw new RuntimeException("Ошибка удаления записи о студенте по id в базе данных.", e);
        }
    }
}
