package ru.natali.courses.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.natali.courses.model.Course;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles("test")
@Transactional
public class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    public void setUp() {
        // Очищаем данные перед каждым тестом
        courseRepository.deleteAll();
    }

    @Test
    public void shouldFindAllCourses() {

        // Given
        Course course1 = new Course();
        course1.setName("Java Fundamentals");
        course1.setStartDate(LocalDate.now());
        course1.setActive(true);
        course1.setArchived(false);

        Course course2 = new Course();
        course2.setName("Spring Boot Advanced");
        course2.setStartDate(LocalDate.now().plusDays(10));
        course2.setActive(true);
        course2.setArchived(false);

        courseRepository.saveAll(List.of(course1, course2));

        // When
        List<Course> courses = courseRepository.findAll();

        // Then
        assertThat(courses).hasSize(2);
        assertThat(courses).extracting(Course::getName)
                .containsExactlyInAnyOrder("Java Fundamentals", "Spring Boot Advanced");
    }
    @Test
    public void shouldFindAllActiveCourses() {
        // Given
        Course activeCourse1 = createTestCourse("Java Fundamentals", true, false);
        Course activeCourse2 = createTestCourse("Spring Boot", true, false);
        Course archivedCourse = createTestCourse("Archived Course", false, true);

        courseRepository.saveAll(List.of(activeCourse1, activeCourse2, archivedCourse));

        // When
        List<Course> activeCourses = courseRepository.findByActiveTrue();

        // Then
        assertThat(activeCourses).hasSize(2);
        assertThat(activeCourses).extracting(Course::getName)
                .containsExactlyInAnyOrder("Java Fundamentals", "Spring Boot");
        assertThat(activeCourses).extracting(Course::isActive)
                .containsOnly(true);
        assertThat(activeCourses).extracting(Course::isArchived)
                .containsOnly(false);
    }



    @Test
    public void shouldReturnEmptyListWhenNoActiveCourses() {
        // Given
        Course archivedCourse1 = createTestCourse("Archived 1", false, true);
        Course archivedCourse2 = createTestCourse("Archived 2", false, true);
        courseRepository.saveAll(List.of(archivedCourse1, archivedCourse2));

        // When
        List<Course> activeCourses = courseRepository.findByActiveTrue();

        // Then
        assertThat(activeCourses).isEmpty();
    }

    @Test
    public void shouldSaveNewCourse() {
        // Given
        Course newCourse = new Course();
        newCourse.setName("New Course");
        newCourse.setStartDate(LocalDate.now());
        newCourse.setActive(true);
        newCourse.setArchived(false);

        // When
        Course savedCourse = courseRepository.save(newCourse);

        // Then
        assertThat(savedCourse).isNotNull();
        assertThat(savedCourse.getId()).isNotNull();
        assertThat(savedCourse.getName()).isEqualTo("New Course");
        assertThat(savedCourse.isActive()).isTrue();
        assertThat(savedCourse.isArchived()).isFalse();
    }

    @Test
    public void shouldFindCourseById() {
        // Given
        Course course = createTestCourse("Find Me", true, false);
        Course savedCourse = courseRepository.save(course);

        // When
        Optional<Course> foundCourse = courseRepository.findById(savedCourse.getId());

        // Then
        assertThat(foundCourse).isPresent();
        assertThat(foundCourse.get().getName()).isEqualTo("Find Me");
    }

    @Test
    public void shouldUpdateExistingCourse() {
        // Given
        Course originalCourse = createTestCourse("Original Name", true, false);
        Course savedCourse = courseRepository.save(originalCourse);

        // When
        savedCourse.setName("Updated Name");
        savedCourse.setActive(false);
        Course updatedCourse = courseRepository.save(savedCourse);

        // Then
        assertThat(updatedCourse.getName()).isEqualTo("Updated Name");
        assertThat(updatedCourse.isActive()).isFalse();
        assertThat(updatedCourse.getId()).isEqualTo(savedCourse.getId());
    }

    @Test
    public void shouldDeleteCourse() {
        // Given
        Course course = createTestCourse("To Be Deleted", true, false);
        Course savedCourse = courseRepository.save(course);

        // When
        courseRepository.deleteById(savedCourse.getId());

        // Then
        assertThat(courseRepository.findById(savedCourse.getId())).isEmpty();
    }

    @Test
    public void shouldNotFindNonExistingCourse() {
        // When
        Optional<Course> foundCourse = courseRepository.findById(999L);

        // Then
        assertThat(foundCourse).isEmpty();
    }

    @Test
    public void shouldNotFailWhenDeletingNonExistingCourse() {
        // When
        assertThatNoException().isThrownBy(() -> courseRepository.deleteById(999L));
    }

    @Test
    public void shouldUpdateOnlySpecificFields() {
        // Given
        Course originalCourse = createTestCourse("Original", true, false);
        originalCourse.setStartDate(LocalDate.of(2023, 1, 1));
        Course savedCourse = courseRepository.save(originalCourse);

        // When - обновляем только имя
        savedCourse.setName("Updated");
        Course updatedCourse = courseRepository.save(savedCourse);

        // Then - проверяем, что другие поля не изменились
        assertThat(updatedCourse.getName()).isEqualTo("Updated");
        assertThat(updatedCourse.getStartDate()).isEqualTo(LocalDate.of(2023, 1, 1));
        assertThat(updatedCourse.isActive()).isTrue();
        assertThat(updatedCourse.isArchived()).isFalse();
    }

    @Test
    public void shouldArchiveCourse() {
        // Given
        Course activeCourse = createTestCourse("Active Course", true, false);
        Course savedCourse = courseRepository.save(activeCourse);

        // When
        savedCourse.setArchived(true);
        savedCourse.setActive(false);
        Course archivedCourse = courseRepository.save(savedCourse);

        // Then
        assertThat(archivedCourse.isArchived()).isTrue();
        assertThat(archivedCourse.isActive()).isFalse();
    }

    private Course createTestCourse(String name, boolean active, boolean archived) {
        Course course = new Course();
        course.setName(name);
        course.setStartDate(LocalDate.now());
        course.setActive(active);
        course.setArchived(archived);
        return course;
    }
}