package ru.natali.courses.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.natali.courses.dto.SimpleCourseDTO;
import ru.natali.courses.model.Course;
import ru.natali.courses.model.Student;
import ru.natali.courses.service.CourseService;
import ru.natali.courses.service.StudentService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/active")
    public List<Course> getActiveCourses() {
        return courseService.getActiveCourses();
    }

    @GetMapping("/archived")
    public List<Course> getArchivedCourses() {
        return courseService.getArchivedCourses();
    }

    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
    }

    @PostMapping()
    public Course createCourse(@RequestBody Course course) {
        return courseService.createCourse(course);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Course> updateCourse(@PathVariable Long id, @RequestBody Course courseDetails) {
        Course updatedCourse = courseService.updateCourse(id, courseDetails);
        return ResponseEntity.ok(updatedCourse);
    }

    @GetMapping("/{courseId}/students")
    public List<Student> getStudentsByCourse(@PathVariable Long courseId) {
        // Используем метод getCourseById из CourseService
        Course course = courseService.getCourseById(courseId);
        return studentService.getStudentsByCourse(course);
    }
    @PutMapping("/students/{studentId}")
    public ResponseEntity<Student> updateStudent(@PathVariable Long studentId, @RequestBody Student studentDetails) {
        Student updatedStudent = studentService.updateStudent(studentId, studentDetails);
        return ResponseEntity.ok(updatedStudent);
    }

    @PostMapping("/students")
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }
    // Эндпоинт для получения отчета по студентам
    @GetMapping("/{courseId}/students-by-last-name-starts-with/{letter}")
    public List<Student> getStudentsByCourseAndLastNameStartsWith(
            @PathVariable Long courseId,
            @PathVariable char letter
    ) {
        Course course = courseService.getCourseById(courseId);
        return studentService.findStudentsByCourseAndLastNameStartsWith(course, letter);
    }

    //Отчет "Статистика по курсам" (количество студентов, даты, активность)
    //GET http://localhost:8080/api/courses/statistics
    @GetMapping("/statistics")
    public List<Map<String, Object>> getCourseStatistics() {
        return courseService.getCourseStatisticsReport();
    }

    //GET http://localhost:8080/api/courses/empty-assignments
    @GetMapping("/empty-assignments")
    public Map<String, Object> getEmptyAssignmentsReport() {
        return courseService.getEmptyAssignmentsReport();
    }

    //GET http://localhost:8080/api/courses/by-month
    @GetMapping("/by-month")
    public Map<String, Long> getCoursesByMonth() {
        return courseService.getCoursesByMonthReport();
    }

    //GET http://localhost:8080/api/courses/full-report
    @GetMapping("/full-report")
    public Map<String, Object> getFullReport() {
        Map<String, Object> report = new HashMap<>();

        // Статистика по курсам
        report.put("courseStatistics", courseService.getCourseStatisticsReport());

        // Количество активных/неактивных курсов
        report.put("activeCoursesCount", courseService.getActiveCourses().size());
        report.put("archivedCoursesCount", courseService.getArchivedCourses().size());

        // Распределение по месяцам
        report.put("coursesByMonth", courseService.getCoursesByMonthReport());

        // Проблемные назначения
        report.put("emptyAssignments", courseService.getEmptyAssignmentsReport());

        return report;
    }

    //GET http://localhost:8080/api/courses/1/last-name-distribution
    @GetMapping("/{courseId}/last-name-distribution")
    public Map<Character, Long> getLastNameInitialDistribution(@PathVariable Long courseId) {
        return courseService.getLastNameInitialDistribution(courseId);
    }

    /**
     * Получение списка курсов, на которые подписан студент.
     *
     * @param studentId Идентификатор студента
     * @return Список курсов, на которые подписан студент
     */
    //GET http://localhost:8080/api/courses/students/3/courses
    @GetMapping("/students/{studentId}/courses")
    public List<SimpleCourseDTO> getCoursesForStudent(@PathVariable Long studentId) {
        return courseService.getCoursesForStudent(studentId);
    }
}
/*
GET http://localhost:8080/api/courses
DELETE http://localhost:8080/api/courses/3  //удаляет в архив, просто ставит значение false
GET http://localhost:8080/api/courses/active
GET http://localhost:8080/api/courses/archived

POST http://localhost:8080/api/courses/students
{
  "name": "Ivan",
  "lastName": "Bunin"
}

POST http://localhost:8080/api/courses
{
  "name": "New Example Course",
  "startDate": "2025-12-05",
  "active": true
}
PUT http://localhost:8080/api/courses/2
{
  "name": "Updated Example Course",
  "startDate": "2023-11-01",
  "active": false
}
//Русская буква оба варианта сработали
GET http://localhost:8080/api/courses/2/students-by-last-name-starts-with/Б
GET http://localhost:8080/api/courses/2/students-by-last-name-starts-with/%D0%91
 */