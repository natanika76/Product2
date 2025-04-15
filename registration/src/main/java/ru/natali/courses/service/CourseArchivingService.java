package ru.natali.courses.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.natali.courses.model.Course;
import ru.natali.courses.repository.CourseRepository;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CourseArchivingService {

    private final CourseRepository courseRepository;

    public CourseArchivingService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void archiveOldCourses() {
        Date today = new Date();
        List<Course> coursesToArchive = courseRepository.findByStartDateBeforeAndArchivedFalse(today);

        coursesToArchive.forEach(course -> {
            course.setArchived(true);
            course.setActive(false); // возможно, вы хотите также деактивировать архивные курсы
        });

        courseRepository.saveAll(coursesToArchive);
    }

    // Дополнительный метод для ручной архивации
    public void archiveCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new EntityNotFoundException("Course not found"));
        course.setArchived(true);
        course.setActive(false);
        courseRepository.save(course);
    }
}
