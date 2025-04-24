package ru.natali.courses.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.natali.courses.dto.SimpleCourseDTO;
import ru.natali.courses.model.Course;
import ru.natali.courses.model.Student;
import ru.natali.courses.repository.CourseRepository;
import ru.natali.courses.repository.StudentRepository;

import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Основной сервис для работы с курсами.
 */
@Service
public class CourseService {
    @Autowired
    private WebClient webClient;

    /**
     * Получает активные курсы из микросервиса.
     * @return список активных курсов
     */
    public List<Course> getActiveCoursesFromMicroservice() {
        String url = "http://course-management-service:8081/api/courses/active";
        Mono<List<Course>> result = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Course>>() {});
        return result.block();
    }

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    /**
     * Получает все курсы.
     * @return список всех курсов
     */
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    /**
     * Получает активные курсы.
     * @return список активных курсов
     */
    public List<Course> getActiveCourses() {
        return courseRepository.findByActiveTrue();
    }

    /**
     * Получает архивные курсы.
     * @return список архивных курсов
     */
    public List<Course> getArchivedCourses() {
        return courseRepository.findByActiveFalse();
    }

    /**
     * Создает новый курс.
     * @param course данные курса
     * @return созданный курс
     */
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    /**
     * Удаляет курс (деактивирует).
     * @param id идентификатор курса
     */
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id).get();
        course.setActive(false);
        courseRepository.save(course);
    }

    /**
     * Обновляет данные курса.
     * @param id идентификатор курса
     * @param courseDetails новые данные курса
     * @return обновленный курс
     */
    public Course updateCourse(Long id, Course courseDetails) {
        Optional<Course> optionalCourse = courseRepository.findById(id);

        if (optionalCourse.isPresent()) {
            Course existingCourse = optionalCourse.get();
            existingCourse.setName(courseDetails.getName());
            existingCourse.setStartDate(courseDetails.getStartDate());
            existingCourse.setActive(courseDetails.isActive());
            return courseRepository.save(existingCourse);
        } else {
            throw new RuntimeException("Курс с ID " + id + " не найден");
        }
    }

    /**
     * Получает курс по идентификатору.
     * @param courseId идентификатор курса
     * @return найденный курс
     */
    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Курс с ID " + courseId + " не найден"));
    }

    /**
     * Получает статистический отчет по курсам.
     * @return список карт с данными статистики
     */
    public List<Map<String, Object>> getCourseStatisticsReport() {
        return courseRepository.getCourseStatistics().stream()
                .map(result -> {
                    Map<String, Object> stats = new HashMap<>();
                    Course course = (Course) result[0];
                    Long studentCount = (Long) result[1];

                    stats.put("courseId", course.getId());
                    stats.put("courseName", course.getName());
                    stats.put("startDate", course.getStartDate());
                    stats.put("active", course.isActive());
                    stats.put("studentCount", studentCount);

                    return stats;
                })
                .collect(Collectors.toList());
    }

    /**
     * Получает распределение студентов по начальным буквам фамилий.
     * @param courseId идентификатор курса
     * @return карта с распределением (буква → количество)
     */
    public Map<Character, Long> getLastNameInitialDistribution(Long courseId) {
        Course course = getCourseById(courseId);
        return studentRepository.getLastNameInitialDistribution(course).stream()
                .collect(Collectors.toMap(
                        result -> ((String) result[0]).charAt(0),
                        result -> (Long) result[1]
                ));
    }

    /**
     * Получает отчет о незаполненных назначениях.
     * @return карта с данными отчета
     */
    public Map<String, Object> getEmptyAssignmentsReport() {
        Map<String, Object> report = new HashMap<>();
        report.put("studentsWithoutCourses", studentRepository.findByCourseIsNull());
        report.put("coursesWithoutStudents", courseRepository.findCoursesWithoutStudents());
        return report;
    }

    /**
     * Получает отчет о курсах по месяцам.
     * @return карта с распределением (месяц → количество)
     */
    public Map<String, Long> getCoursesByMonthReport() {
        return courseRepository.getCoursesByMonth().stream()
                .collect(Collectors.toMap(
                        result -> Month.of((Integer) result[0]).toString(),
                        result -> (Long) result[1]
                ));
    }

    /**
     * Получает список курсов для студента.
     * @param studentId идентификатор студента
     * @return список курсов в формате DTO
     */
    public List<SimpleCourseDTO> getCoursesForStudent(Long studentId) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            Set<Course> courses = student.getEnrolledCourses();

            return courses.stream()
                    .map(this::convertToSimpleCourseDTO)
                    .collect(Collectors.toList());
        } else {
            throw new EntityNotFoundException("Студент с таким ID не найден");
        }
    }

    private SimpleCourseDTO convertToSimpleCourseDTO(Course course) {
        SimpleCourseDTO dto = new SimpleCourseDTO();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setStartDate(course.getStartDate().toString());
        dto.setActive(course.isActive());
        return dto;
    }
}
