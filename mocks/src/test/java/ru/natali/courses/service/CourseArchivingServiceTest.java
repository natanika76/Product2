package ru.natali.courses.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.natali.courses.model.Course;
import ru.natali.courses.repository.CourseRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseArchivingServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseArchivingService courseArchivingService;

    @Test
    void archiveOldCourses_ShouldArchiveCoursesWithStartDateBeforeToday() {
        // Arrange
        LocalDate today = LocalDate.now(); // Получаем сегодняшнюю дату
        LocalDate pastDate = today.minusDays(1); // Устанавливаем вчерашнюю дату

        Course course1 = new Course();
        course1.setId(1L);
        course1.setName("Test Course 1");
        course1.setStartDate(pastDate);
        course1.setActive(true);
        course1.setArchived(false);

        Course course2 = new Course();
        course2.setId(2L);
        course2.setName("Test Course 2");
        course2.setStartDate(pastDate);
        course2.setActive(true);
        course2.setArchived(false);

        when(courseRepository.findByStartDateBeforeAndArchivedFalse(today))
                .thenReturn(List.of(course1, course2));

        // Act
        courseArchivingService.archiveOldCourses();

        // Assert
        assertTrue(course1.isArchived());   // Проверяем, что курсы архивированы
        assertFalse(course1.isActive());    // Проверяем, что активность снята
        assertTrue(course2.isArchived());
        assertFalse(course2.isActive());

        verify(courseRepository).saveAll(argThat((List<Course> list) ->
                list.size() == 2 &&
                        list.stream().allMatch(c -> c.isArchived() && !c.isActive())
        ));
    }

    @Test
    void archiveCourse_WhenCourseExists_ShouldArchiveCourse() {
        // Arrange
        Long courseId = 1L;
        Course course = new Course();
        course.setId(courseId);
        course.setArchived(false);
        course.setActive(true);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // Act
        courseArchivingService.archiveCourse(courseId);

        // Assert
        assertTrue(course.isArchived());
        assertFalse(course.isActive());
        verify(courseRepository).save(course);
    }

    @Test
    void archiveCourse_WhenCourseNotFound_ShouldThrowException() {
        // Arrange
        Long courseId = 999L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () ->
                courseArchivingService.archiveCourse(courseId)
        );
    }
}