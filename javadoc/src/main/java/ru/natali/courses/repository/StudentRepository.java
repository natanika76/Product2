package ru.natali.courses.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.natali.courses.model.Course;
import ru.natali.courses.model.Student;

import java.util.List;
import java.util.Optional;

/**
 * Репозиторий для работы со студентами.
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    /**
     * Находит студентов по курсу.
     * @param course курс для поиска
     * @return список студентов на указанном курсе
     */
    List<Student> findByCourse(Course course);

    /**
     * Находит студентов по курсу и начальной букве фамилии.
     * @param course курс для поиска
     * @param initialLetter начальная буква фамилии
     * @return список подходящих студентов
     */
    @Query("SELECT s FROM Student s WHERE s.course = :course AND LOWER(s.lastName) LIKE CONCAT(LOWER(:initialLetter), '%')")
    List<Student> findByCourseAndLastNameStartingWithIgnoreCase(@Param("course") Course course, @Param("initialLetter") String initialLetter);

    /**
     * Получает распределение студентов по начальным буквам фамилий.
     * @param course курс для анализа
     * @return список массивов объектов, содержащих букву и количество студентов
     */
    @Query("SELECT UPPER(SUBSTRING(s.lastName, 1, 1)) as initial, COUNT(s) " +
            "FROM Student s WHERE s.course = :course " +
            "GROUP BY UPPER(SUBSTRING(s.lastName, 1, 1)) " +
            "ORDER BY initial")
    List<Object[]> getLastNameInitialDistribution(@Param("course") Course course);

    /**
     * Находит студентов без курса.
     * @return список студентов без курса
     */
    List<Student> findByCourseIsNull();

    /**
     * Находит студента по имени пользователя.
     * @param username имя пользователя
     * @return Optional с найденным студентом
     */
    Optional<Student> findByUsername(String username);
}
