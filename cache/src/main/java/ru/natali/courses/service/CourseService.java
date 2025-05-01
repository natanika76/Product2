package ru.natali.courses.service;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.natali.courses.dto.CourseBasicDTO;
import ru.natali.courses.dto.EmptyAssignmentReportDTO;
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
    private CourseRepository courseRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Cacheable(value = "courses", key = "'all'")
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Cacheable(value = "courses", key = "'archived'")
    public List<Course> getArchivedCourses() {
        return courseRepository.findByActiveFalse();
    }

    @CacheEvict(value = "courses", allEntries = true)
    @Transactional
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    @Cacheable(value = "courses", key = "'active'")
    public List<CourseBasicDTO> getActiveCourses() {
        return courseRepository.findByActiveTrue().stream()
                .map(CourseBasicDTO::new)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = {"courses", "course"}, key = "#id")
    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id).get();
        course.setActive(false);
        courseRepository.save(course);
    }

    @CachePut(value = "course", key = "#id")
    @CacheEvict(value = "courses", allEntries = true)
    public Course updateCourse(Long id, CourseBasicDTO courseDetails) {
    Optional<Course> optionalCourse = courseRepository.findById(id);

    if (optionalCourse.isPresent()) {
        Course existingCourse = optionalCourse.get();

        // Обновляем только те поля, которые пришли в DTO
        if (courseDetails.getName() != null) {
            existingCourse.setName(courseDetails.getName());
        }
        if (courseDetails.getStartDate() != null) {
            existingCourse.setStartDate(courseDetails.getStartDate());
        }
        // Активность обновляем всегда, так как это boolean
        existingCourse.setActive(courseDetails.isActive());

        return courseRepository.save(existingCourse);
    } else {
        throw new RuntimeException("Курс с ID " + id + " не найден");
    }
}

    // Добавляем метод для получения курса по идентификатору
    @Cacheable(value = "course", key = "#courseId")
    public Course getCourseById(Long courseId) {
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Курс с ID " + courseId + " не найден"));
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

    public EmptyAssignmentReportDTO getEmptyAssignmentsReport() {
        List<Student> studentsWithoutCourses = studentRepository.findByCourseIsNull();
        List<Course> coursesWithoutStudents = courseRepository.findCoursesWithoutStudents();

        return new EmptyAssignmentReportDTO(studentsWithoutCourses, coursesWithoutStudents);
    }

    public Map<String, Long> getCoursesByMonthReport() {
        return courseRepository.getCoursesByMonth().stream()
                .collect(Collectors.toMap(
                        result -> Month.of((Integer) result[0]).toString(),
                        result -> (Long) result[1]
                ));
    }

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

    // В классе CourseService добавляем:
    public Map<String, Object> getFullReport() {
        Map<String, Object> report = new HashMap<>();
        report.put("courseStatistics", this.getCourseStatisticsReport());
        report.put("activeCoursesCount", this.getActiveCourses().size());
        report.put("archivedCoursesCount", this.getArchivedCourses().size());
        report.put("coursesByMonth", this.getCoursesByMonthReport());
        report.put("emptyAssignments", this.getEmptyAssignmentsReport());
        return report;
    }
    // Вспомогательный метод для преобразования Course в SimpleCourseDTO
    /*
    private SimpleCourseDTO convertToSimpleCourseDTO(Course course) { // Важно разместить метод здесь
        SimpleCourseDTO dto = new SimpleCourseDTO();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.getStartDate();
        dto.setActive(course.isActive());
        return dto;
    }*/
    private SimpleCourseDTO convertToSimpleCourseDTO(Course course) {
        SimpleCourseDTO dto = new SimpleCourseDTO();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setStartDate(course.getStartDate());  // Добавьте эту строку
        dto.setActive(course.isActive());
        return dto;
    }
}