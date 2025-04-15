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

@Service
public class CourseService {

    @Autowired
    private WebClient webClient;

    public List<Course> getActiveCoursesFromMicroservice() {
        String url = "http://course-management-service:8081/api/courses/active";
        Mono<List<Course>> result = webClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<Course>>() {});
        return result.block(); // Блокирующий вызов для получения результата
    }

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public List<Course> getActiveCourses() {
        return courseRepository.findByActiveTrue();
    }

    public List<Course> getArchivedCourses() {
        return courseRepository.findByActiveFalse();
    }

    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id).get();
        course.setActive(false); // Архивируем курс
        courseRepository.save(course);
    }

    public Course updateCourse(Long id, Course courseDetails) {
        // Загружаем существующий курс по ID
        Optional<Course> optionalCourse = courseRepository.findById(id);

        if (optionalCourse.isPresent()) {
            Course existingCourse = optionalCourse.get();

            // Обновляем необходимые поля
            existingCourse.setName(courseDetails.getName());          // Название курса
            existingCourse.setStartDate(courseDetails.getStartDate()); // Дата начала
            existingCourse.setActive(courseDetails.isActive());       // Активность курса

            // Сохраняем изменения
            return courseRepository.save(existingCourse);
        } else {
            throw new RuntimeException("Курс с ID " + id + " не найден");
        }
    }

    // Добавляем метод для получения курса по идентификатору
    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId).orElseThrow(() -> new RuntimeException("Курс с ID " + courseId + " не найден"));
    }

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

    public Map<Character, Long> getLastNameInitialDistribution(Long courseId) {
        Course course = getCourseById(courseId);
        return studentRepository.getLastNameInitialDistribution(course).stream()
                .collect(Collectors.toMap(
                        result -> ((String) result[0]).charAt(0),
                        result -> (Long) result[1]
                ));
    }
   public Map<String, Object> getEmptyAssignmentsReport() {
       Map<String, Object> report = new HashMap<>();
       report.put("studentsWithoutCourses", studentRepository.findByCourseIsNull());
       report.put("coursesWithoutStudents", courseRepository.findCoursesWithoutStudents());
       return report;
   }

    public Map<String, Long> getCoursesByMonthReport() {
        return courseRepository.getCoursesByMonth().stream()
                .collect(Collectors.toMap(
                        result -> Month.of((Integer) result[0]).toString(),
                        result -> (Long) result[1]
                ));
    }

    /**
     * Возвращает список курсов, на которые подписан студент, в виде DTO.
     *
     * @param studentId идентификатор студента
     * @return список курсов
     */
    public List<SimpleCourseDTO> getCoursesForStudent(Long studentId) {
        Optional<Student> studentOptional = studentRepository.findById(studentId);

        if (studentOptional.isPresent()) {
            Student student = studentOptional.get();
            Set<Course> courses = student.getEnrolledCourses();

            // Преобразовываем курсы в DTO
            return courses.stream()
                    .map(this::convertToSimpleCourseDTO) // Привязываем внутренний метод
                    .collect(Collectors.toList());
        } else {
            throw new EntityNotFoundException("Студент с таким ID не найден");
        }
    }

    // Вспомогательный метод для преобразования Course в SimpleCourseDTO
    private SimpleCourseDTO convertToSimpleCourseDTO(Course course) { // Важно разместить метод здесь
        SimpleCourseDTO dto = new SimpleCourseDTO();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setStartDate(course.getStartDate().toString()); // Предположим, что startDate — это java.util.Date
        dto.setActive(course.isActive());
        return dto;
    }
}