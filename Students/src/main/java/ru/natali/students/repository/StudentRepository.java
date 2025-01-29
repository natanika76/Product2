package ru.natali.students.repository;

import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.natali.students.model.Student;

import java.util.List;

@Repository
public class StudentRepository {

    private final JdbcTemplate jdbcTemplate;

    public StudentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Получение списка всех студентов
    public List<Student> findAll() {
        try {
            return jdbcTemplate.query("SELECT * FROM students", new StudentRowMapper());
        } catch (DataAccessException e) {
            throw new RuntimeException("Ошибка извлечения данных всех студентов из базы данных.", e);
        }
    }

    // Поиск студента по ID
    public Optional<Student> findById(Long id) {
        try {
            List<Student> results = jdbcTemplate.query("SELECT * FROM students WHERE id = ?", new StudentRowMapper(), id);
            return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
        } catch (DataAccessException e) {
            throw new RuntimeException("Ошибка поиска студента по id в базе данных.", e);
        }
    }

    // Сохранение нового студента
    public Student save(Student student) {
        try {
            jdbcTemplate.update(
                    "INSERT INTO students (full_name, email, courses) VALUES (?, ?, ?)",
                    student.getFullName(),
                    student.getEmail(),
                    String.join(",", student.getCourses())
            );

            // Возвращаем сохраненный объект с присвоенным ID
            return student;
        } catch (DataAccessException e) {
            throw new RuntimeException("Ошибка сохранения студента в базу данных.", e);
        }
    }

    // Обновление информации о студенте
    public int update(Student student) {
        try {
            return jdbcTemplate.update(
                    "UPDATE students SET full_name = ?, email = ?, courses = ? WHERE id = ?",
                    student.getFullName(),
                    student.getEmail(),
                    String.join(",", student.getCourses()),
                    student.getId()
            );
        } catch (DataAccessException e) {
            throw new RuntimeException("Ошибка обновления информации о студенте в базе данных.", e);
        }
    }

    // Удаление студента по ID
    public void deleteById(Long id) {
        try {
            jdbcTemplate.update("DELETE FROM students WHERE id = ?", id);
        } catch (DataAccessException e) {
            throw new RuntimeException("Ошибка удаления записи о студенте по id в базе данных.", e);
        }
    }
}