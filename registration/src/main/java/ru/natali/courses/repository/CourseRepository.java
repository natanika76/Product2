package ru.natali.courses.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.natali.courses.model.Course;

import java.util.Date;
import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    //Найти активные курсы
    List<Course> findByActiveTrue();

    //Найти неактивные курсы (в архиве)
    List<Course> findByActiveFalse();

    @Query("SELECT c, COUNT(s) FROM Course c LEFT JOIN Student s ON s.course = c GROUP BY c")
    List<Object[]> getCourseStatistics();

    @Query("SELECT c FROM Course c WHERE NOT EXISTS (SELECT s FROM Student s WHERE s.course = c)")
    List<Course> findCoursesWithoutStudents();

    @Query("SELECT MONTH(c.startDate) as month, COUNT(c) as count " +
                  "FROM Course c " +
                  "GROUP BY MONTH(c.startDate) " +
                  "ORDER BY month")
    List<Object[]> getCoursesByMonth();

    List<Course> findByStartDateBeforeAndArchivedFalse(Date date);
}
