package ru.natali.courses.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.natali.courses.model.Course;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {
    //Найти активные курсы
    List<Course> findByActiveTrue();

    //Найти неактивные курсы (в архиве)
    List<Course> findByActiveFalse();
}
