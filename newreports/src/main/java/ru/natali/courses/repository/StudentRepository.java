package ru.natali.courses.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.natali.courses.model.Course;
import ru.natali.courses.model.Student;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByCourse(Course course);

    // Метод для поиска студентов по курсу и начальной букве фамилии
    @Query("SELECT s FROM Student s WHERE s.course = :course AND LOWER(s.lastName) LIKE CONCAT(LOWER(:initialLetter), '%')")
    List<Student> findByCourseAndLastNameStartingWithIgnoreCase(@Param("course") Course course, @Param("initialLetter") String initialLetter);

    @Query("SELECT UPPER(SUBSTRING(s.lastName, 1, 1)) as initial, COUNT(s) " +
            "FROM Student s WHERE s.course = :course " +
            "GROUP BY UPPER(SUBSTRING(s.lastName, 1, 1)) " +
            "ORDER BY initial")
    List<Object[]> getLastNameInitialDistribution(@Param("course") Course course);

    List<Student> findByCourseIsNull();
}
