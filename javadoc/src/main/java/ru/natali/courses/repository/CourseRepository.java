package ru.natali.courses.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.natali.courses.model.Course;

import java.util.Date;
import java.util.List;

/**
 * Репозиторий для работы с курсами.
 */
public interface CourseRepository extends JpaRepository<Course, Long> {
    /**
     * Находит активные курсы.
     * @return список активных курсов
     */
    List<Course> findByActiveTrue();

    /**
     * Находит неактивные курсы.
     * @return список неактивных курсов
     */
    List<Course> findByActiveFalse();

    /**
     * Получает статистику по курсам.
     * @return список массивов объектов, содержащих курс и количество студентов
     */
    @Query("SELECT c, COUNT(s) FROM Course c LEFT JOIN Student s ON s.course = c GROUP BY c")
    List<Object[]> getCourseStatistics();

    /**
     * Находит курсы без студентов.
     * @return список курсов без студентов
     */
    @Query("SELECT c FROM Course c WHERE NOT EXISTS (SELECT s FROM Student s WHERE s.course = c)")
    List<Course> findCoursesWithoutStudents();

    /**
     * Получает распределение курсов по месяцам.
     * @return список массивов объектов, содержащих месяц и количество курсов
     */
    @Query("SELECT MONTH(c.startDate) as month, COUNT(c) as count " +
            "FROM Course c " +
            "GROUP BY MONTH(c.startDate) " +
            "ORDER BY month")
    List<Object[]> getCoursesByMonth();

    /**
     * Находит курсы с датой начала раньше указанной и неархивированные.
     * @param date дата для сравнения
     * @return список подходящих курсов
     */
    List<Course> findByStartDateBeforeAndArchivedFalse(Date date);
}