package ru.natali.courses.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.natali.courses.dto.CourseBasicDTO;
import ru.natali.courses.dto.EmptyAssignmentReportDTO;
import ru.natali.courses.dto.SimpleCourseDTO;
import ru.natali.courses.model.Course;
import ru.natali.courses.model.Student;
import ru.natali.courses.repository.CourseRepository;
import ru.natali.courses.repository.StudentRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private CourseService courseService;

    private Course testCourse;
    private Student testStudent;

    @BeforeEach
    void setUp() {
        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setName("Java Programming");
        testCourse.setStartDate(LocalDate.of(2024, 10, 12));
        testCourse.setActive(true);

        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setFirstName("John");
        testStudent.setLastName("Doe");
        testCourse.setStudents(new HashSet<>(Arrays.asList(testStudent)));
    }
/* //Старый тест, ниже модернизирован с Hamcrest
    @Test
    void getAllCourses_ShouldReturnAllCourses() {
        when(courseRepository.findAll()).thenReturn(Arrays.asList(testCourse));
        List<Course> allCourses = courseService.getAllCourses();
        assertEquals(1, allCourses.size());
        assertEquals("Java Programming", allCourses.get(0).getName());
    }
*/
    @Test
    void getAllCourses_ShouldReturnAllCourses() {
        when(courseRepository.findAll()).thenReturn(Arrays.asList(testCourse));
        List<Course> allCourses = courseService.getAllCourses();

        assertThat(allCourses, hasSize(1));
        assertThat(allCourses.get(0).getName(), is("Java Programming"));
        assertThat(allCourses, contains(hasProperty("active", is(true))));
    }
    @Test
    void getArchivedCourses_ShouldReturnOnlyInactiveCourses() {
        Course inactiveCourse = new Course();
        inactiveCourse.setId(2L);
        inactiveCourse.setName("Python Basics");
        inactiveCourse.setActive(false);

        when(courseRepository.findByActiveFalse()).thenReturn(Arrays.asList(inactiveCourse));
        List<Course> archivedCourses = courseService.getArchivedCourses();
        assertEquals(1, archivedCourses.size());
        assertFalse(archivedCourses.get(0).isActive());
    }
/*
    @Test
    void createCourse_ShouldCreateNewCourse() {
        Course newCourse = new Course();
        newCourse.setName("C++ Advanced");
        newCourse.setStartDate(LocalDate.of(2025, 11, 18));
        newCourse.setActive(true);

        when(courseRepository.save(any())).thenReturn(newCourse);
        Course createdCourse = courseService.createCourse(newCourse);
        assertNotNull(createdCourse);
        assertEquals("C++ Advanced", createdCourse.getName());
    }
*/
@ParameterizedTest
@CsvFileSource(resources = "/course_test_data.csv", numLinesToSkip = 1)
void createCourse_ShouldCreateNewCourse_FromCsv(
        Long id, String name, LocalDate startDate, boolean active) {
    Course newCourse = new Course();
    newCourse.setName(name);
    newCourse.setStartDate(startDate);
    newCourse.setActive(active);

    when(courseRepository.save(any())).thenReturn(newCourse);

    Course createdCourse = courseService.createCourse(newCourse);
    assertNotNull(createdCourse);
    assertEquals(name, createdCourse.getName());
    assertEquals(active, createdCourse.isActive());
}
    @Test
    void getActiveCourses_ShouldReturnOnlyActiveCourses() {
        when(courseRepository.findByActiveTrue()).thenReturn(Arrays.asList(testCourse));
        List<CourseBasicDTO> activeCourses = courseService.getActiveCourses();
        assertEquals(1, activeCourses.size());
        assertTrue(activeCourses.get(0).isActive());
    }

    @Test
    void deleteCourse_ShouldDeactivateCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        courseService.deleteCourse(1L);
        assertFalse(testCourse.isActive());
    }

    @Test
    void updateCourse_ShouldUpdateCourseData() {
        CourseBasicDTO courseDetails = new CourseBasicDTO();
        courseDetails.setName("Updated Name");
        courseDetails.setStartDate(LocalDate.of(2025, 11, 18));
        courseDetails.setActive(false);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(courseRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Course updatedCourse = courseService.updateCourse(1L, courseDetails);
        assertEquals("Updated Name", updatedCourse.getName());
        assertFalse(updatedCourse.isActive());
    }
/*
    @Test
    void getCourseById_ShouldReturnCourse_WhenExists() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        Course retrievedCourse = courseService.getCourseById(1L);
        assertEquals("Java Programming", retrievedCourse.getName());
    }
*/
static Stream<Arguments> courseProvider() {
    return Stream.of(
            Arguments.of(1L, "Java Basics", LocalDate.of(2024, 1, 15), true),
            Arguments.of(2L, "Python Advanced", LocalDate.of(2024, 2, 20), false)
    );
}
@ParameterizedTest
@MethodSource("courseProvider")
void getCourseById_ShouldReturnCourse_WhenExists(
        Long id, String name, LocalDate startDate, boolean active) {
    Course testCourse = new Course();
    testCourse.setId(id);
    testCourse.setName(name);
    testCourse.setStartDate(startDate);
    testCourse.setActive(active);

    when(courseRepository.findById(id)).thenReturn(Optional.of(testCourse));

    Course retrievedCourse = courseService.getCourseById(id);
    /*assertEquals(name, retrievedCourse.getName());
    assertEquals(active, retrievedCourse.isActive());
   */
    //можно добавить Hamcrest:
    assertThat(retrievedCourse, allOf(
            hasProperty("name", is(name)),
            hasProperty("active", is(active))
    ));

}
    @Test
    void getCourseById_ShouldThrowException_WhenNotExists() {
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> courseService.getCourseById(999L));
    }

    @Test
    void getCourseStatisticsReport_ShouldReturnValidStats() {
        List<Object[]> mockResult = Arrays.asList(
                new Object[] {testCourse, 1L},
                new Object[] {testCourse, 2L}
        );

        when(courseRepository.getCourseStatistics()).thenReturn(mockResult);
        List<Map<String, Object>> report = courseService.getCourseStatisticsReport();
        assertEquals(2, report.size());
        assertEquals("Java Programming", report.get(0).get("courseName"));
    }

    @Test
    void getLastNameInitialDistribution_ShouldReturnValidDistribution() {
        // Создание курса с нужным ID
        Course testCourse = new Course();
        testCourse.setId(1L); // Важный момент: выставляем ID равным 1
        testCourse.setName("Math Fundamentals");

        // Ожидаемый результат распределений фамилий
        List<Object[]> mockResult = Arrays.asList(
                new Object[]{"A", 2L},
                new Object[]{"B", 1L}
        );

        // Мококируем поведение репозитория:
        when(studentRepository.getLastNameInitialDistribution(testCourse)).thenReturn(mockResult);
        when(courseRepository.findById(1L)) // Ждём курс с правильным ID
                .thenReturn(Optional.of(testCourse));

        // Выполняем тестируемый метод
        Map<Character, Long> distribution = courseService.getLastNameInitialDistribution(1L);

        // Проверка результата
        assertEquals(2, distribution.size());
        assertEquals(2L, distribution.get('A'));
        assertEquals(1L, distribution.get('B'));
    }

    @Test
    void getEmptyAssignmentsReport_ShouldReturnValidReport() {
        List<Student> studentsWithoutCourses = Arrays.asList(testStudent);
        List<Course> coursesWithoutStudents = Arrays.asList(testCourse);

        when(studentRepository.findByCourseIsNull()).thenReturn(studentsWithoutCourses);
        when(courseRepository.findCoursesWithoutStudents()).thenReturn(coursesWithoutStudents);

        EmptyAssignmentReportDTO report = courseService.getEmptyAssignmentsReport();
        assertEquals(1, report.getStudentsWithoutCourses().size());
        assertEquals(1, report.getCoursesWithoutStudents().size());
    }

    @Test
    void getCoursesByMonthReport_ShouldReturnValidMonthlyDistribution() {
        List<Object[]> mockResult = Arrays.asList(
                new Object[] {10, 2L}, // Октябрь
                new Object[] {11, 1L}  // Ноябрь
        );

        when(courseRepository.getCoursesByMonth()).thenReturn(mockResult);
        Map<String, Long> monthlyDistribution = courseService.getCoursesByMonthReport();
        assertEquals(2, monthlyDistribution.size());
        assertEquals(2L, monthlyDistribution.get("OCTOBER"));
        assertEquals(1L, monthlyDistribution.get("NOVEMBER"));
    }

    @Test
    void getCoursesForStudent_ShouldReturnValidCourses() {
        // Настраиваем связи между студентом и курсом
        testStudent.setEnrolledCourses(new HashSet<>(Arrays.asList(testCourse)));

        // Настраиваем моки
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));

        // Запускаем метод
        List<SimpleCourseDTO> courses = courseService.getCoursesForStudent(1L);

        // Проверяем результат
        assertEquals(1, courses.size()); // Должен вернуть ровно один курс
        assertEquals("Java Programming", courses.get(0).getName()); // Название курса
    }

    @Test
    void getCoursesForStudent_ShouldThrowEntityNotFoundException_WhenStudentNotExists() {
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> courseService.getCoursesForStudent(999L));
    }
    //==================================================
    @Test
    void getAllCourses_ShouldReturnEmptyList_WhenNoCoursesExist() {
        when(courseRepository.findAll()).thenReturn(Collections.emptyList());
        List<Course> allCourses = courseService.getAllCourses();
        assertTrue(allCourses.isEmpty());
    }

    @Test
    void getArchivedCourses_ShouldReturnEmptyList_WhenNoArchivedCourses() {
        when(courseRepository.findByActiveFalse()).thenReturn(Collections.emptyList());
        List<Course> archivedCourses = courseService.getArchivedCourses();
        assertTrue(archivedCourses.isEmpty());
    }

    @Test
    void deleteCourse_ShouldThrowException_WhenCourseNotFound() {
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> courseService.deleteCourse(999L));
    }

    @Test
    void updateCourse_ShouldNotChangeUnspecifiedFields() {
        // Создаем тестовый курс с конкретной датой и активным статусом
        LocalDate originalDate = LocalDate.of(2024, 10, 12);
        testCourse.setStartDate(originalDate);
        testCourse.setActive(true); // Явно устанавливаем активный статус

        // Создаем DTO с обновлением только имени
        CourseBasicDTO courseDetails = new CourseBasicDTO();
        courseDetails.setName("Updated Name");
        courseDetails.setActive(true); // Явно устанавливаем активный статус

        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(courseRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Course updatedCourse = courseService.updateCourse(1L, courseDetails);

        // Проверяем, что имя обновилось
        assertEquals("Updated Name", updatedCourse.getName());
        // Проверяем, что дата осталась прежней
        assertEquals(originalDate, updatedCourse.getStartDate());
        // Проверяем, что активность осталась true
        assertTrue(updatedCourse.isActive());
    }
    @Test
    void getCoursesForStudent_ShouldReturnEmptyList_WhenStudentHasNoCourses() {
        testStudent.setEnrolledCourses(new HashSet<>());

        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        List<SimpleCourseDTO> courses = courseService.getCoursesForStudent(1L);
        assertTrue(courses.isEmpty());
    }

    @Test
    void getCoursesForStudent_ShouldCorrectlyConvertCoursesToDTO() {
        // Подготовка тестовых данных
        testCourse.setStartDate(LocalDate.of(2024, 10, 12));
        testStudent.setEnrolledCourses(new HashSet<>(Arrays.asList(testCourse)));

        // Настройка моков
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));

        // Вызов тестируемого метода
        List<SimpleCourseDTO> result = courseService.getCoursesForStudent(1L);

        // Проверки
        assertEquals(1, result.size());

        SimpleCourseDTO dto = result.get(0);
        assertEquals(testCourse.getId(), dto.getId());
        assertEquals(testCourse.getName(), dto.getName());
        assertEquals(LocalDate.of(2024, 10, 12), dto.getStartDate());  // Проверка даты
        assertEquals(testCourse.isActive(), dto.isActive());
    }
}