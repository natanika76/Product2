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
import ru.natali.courses.model.Role;
import ru.natali.courses.model.Student;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Testcontainers
@ActiveProfiles("test")
@Transactional
public class CourseRepositoryAdvancedTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    public void setUp() {
        studentRepository.deleteAll();
        courseRepository.deleteAll();
    }

    @Test
    public void shouldGetCourseStatistics() {
        // Given
        Course course1 = createAndSaveCourse("Java Basics", true, false);
        Course course2 = createAndSaveCourse("Spring Advanced", true, false);

        createAndSaveStudent("John", "Doe", course1);
        createAndSaveStudent("Jane", "Smith", course1);
        createAndSaveStudent("Alex", "Johnson", course2);

        // When
        List<Object[]> statistics = courseRepository.getCourseStatistics();

        // Then
        assertThat(statistics).hasSize(2);

        // Проверяем первую запись
        Object[] firstResult = statistics.get(0);
        Course firstCourse = (Course) firstResult[0];
        Long firstCount = (Long) firstResult[1];

        if (firstCourse.getName().equals("Java Basics")) {
            assertThat(firstCount).isEqualTo(2);
        } else {
            assertThat(firstCount).isEqualTo(1);
        }

        // Проверяем вторую запись
        Object[] secondResult = statistics.get(1);
        Course secondCourse = (Course) secondResult[0];
        Long secondCount = (Long) secondResult[1];

        if (secondCourse.getName().equals("Java Basics")) {
            assertThat(secondCount).isEqualTo(2);
        } else {
            assertThat(secondCount).isEqualTo(1);
        }
    }

    @Test
    public void shouldGetCoursesWithoutStudents() {
        // Given
        Course withStudents = createAndSaveCourse("With Students", true, false);
        Course withoutStudents = createAndSaveCourse("Without Students", true, false);

        createAndSaveStudent("Mike", "Brown", withStudents);

        // When
        List<Course> coursesWithoutStudents = courseRepository.findCoursesWithoutStudents();

        // Then
        assertThat(coursesWithoutStudents).hasSize(1);
        assertThat(coursesWithoutStudents.get(0).getName()).isEqualTo("Without Students");
    }

    @Test
    public void shouldGetCoursesByMonth() {
        // Given
        createAndSaveCourse("January Course", true, false, LocalDate.of(2023, 1, 15));
        createAndSaveCourse("February Course", true, false, LocalDate.of(2023, 2, 20));
        createAndSaveCourse("Another January", true, false, LocalDate.of(2023, 1, 10));

        // When
        List<Object[]> coursesByMonth = courseRepository.getCoursesByMonth();

        // Then
        assertThat(coursesByMonth).hasSize(2);

        // Проверяем январь
        assertThat((Integer) coursesByMonth.get(0)[0]).isEqualTo(1); // месяц
        assertThat((Long) coursesByMonth.get(0)[1]).isEqualTo(2);    // количество

        // Проверяем февраль
        assertThat((Integer) coursesByMonth.get(1)[0]).isEqualTo(2); // месяц
        assertThat((Long) coursesByMonth.get(1)[1]).isEqualTo(1);    // количество
    }

    @Test
    public void shouldFindCoursesBeforeDateAndNotArchived() {
        // Given
        LocalDate testDate = LocalDate.of(2023, 6, 1);

        Course before1 = createAndSaveCourse("Before 1", true, false, LocalDate.of(2023, 5, 1));
        Course before2 = createAndSaveCourse("Before 2", true, false, LocalDate.of(2023, 1, 1));
        Course after = createAndSaveCourse("After", true, false, LocalDate.of(2023, 7, 1));
        Course archived = createAndSaveCourse("Archived", false, true, LocalDate.of(2023, 5, 1));

        // When
        List<Course> foundCourses = courseRepository.findByStartDateBeforeAndArchivedFalse(testDate);

        // Then
        assertThat(foundCourses).hasSize(2);
        assertThat(foundCourses).extracting(Course::getName)
                .containsExactlyInAnyOrder("Before 1", "Before 2");
    }

    @Test
    public void shouldReturnEmptyStatisticsWhenNoCourses() {
        // When
        List<Object[]> statistics = courseRepository.getCourseStatistics();
        List<Course> withoutStudents = courseRepository.findCoursesWithoutStudents();
        List<Object[]> byMonth = courseRepository.getCoursesByMonth();
        List<Course> beforeDate = courseRepository.findByStartDateBeforeAndArchivedFalse(LocalDate.now());

        // Then
        assertThat(statistics).isEmpty();
        assertThat(withoutStudents).isEmpty();
        assertThat(byMonth).isEmpty();
        assertThat(beforeDate).isEmpty();
    }

    @Test
    public void shouldHandleCoursesWithZeroStudentsInStatistics() {
        // Given
        createAndSaveCourse("No Students", true, false);

        // When
        List<Object[]> statistics = courseRepository.getCourseStatistics();

        // Then
        assertThat(statistics).hasSize(1);
        assertThat((Long) statistics.get(0)[1]).isEqualTo(0);
    }

    private Course createAndSaveCourse(String name, boolean active, boolean archived) {
        return createAndSaveCourse(name, active, archived, LocalDate.now());
    }

    private Course createAndSaveCourse(String name, boolean active, boolean archived, LocalDate startDate) {
        Course course = new Course();
        course.setName(name);
        course.setStartDate(startDate);
        course.setActive(active);
        course.setArchived(archived);
        return courseRepository.save(course);
    }

    private Student createAndSaveStudent(String firstName, String lastName, Course course) {
        Student student = new Student();
        student.setFirstName(firstName);
        student.setLastName(lastName);
        student.setCourse(course);
        // Устанавливаем обязательные поля
        student.setEmail(firstName.toLowerCase() + "." + lastName.toLowerCase() + "@example.com");
        student.setUsername(firstName.toLowerCase() + lastName.toLowerCase());
        student.setPassword("password");
        student.setRole(Role.USER);
        return studentRepository.save(student);
    }
}