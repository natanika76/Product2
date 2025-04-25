package ru.natali.courses.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
//http://localhost:8080/swagger-ui/index.html
//http://localhost:8080/v3/api-docs/swagger-ui/    не работает, просит аутентификацию
@RestController
@RequestMapping("/api/courses")
@Tag(name = "Управление курсами", description = "API для работы с учебными курсами")
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private StudentService studentService;

    @GetMapping
    @Operation(summary = "Получить все курсы", description = "Возвращает список всех курсов")
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    @GetMapping("/active")
    @Operation(summary = "Получить активные курсы", description = "Возвращает список активных курсов")
    public List<Course> getActiveCourses() {
        return courseService.getActiveCourses();
    }

    @GetMapping("/archived")
    @Operation(summary = "Получить архивные курсы", description = "Возвращает список архивных курсов")
    public List<Course> getArchivedCourses() {
        return courseService.getArchivedCourses();
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить курс", description = "Удаляет курс по указанному ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Курс успешно удален"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    public void deleteCourse(@PathVariable @Parameter(description = "ID курса", example = "1") Long id) {
        courseService.deleteCourse(id);
    }

    @PostMapping
    @Operation(summary = "Создать новый курс", description = "Создает новый курс с переданными данными")
    @ApiResponse(responseCode = "200", description = "Курс успешно создан")
    public Course createCourse(@RequestBody @Parameter(description = "Данные курса") Course course) {
        return courseService.createCourse(course);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить курс", description = "Обновляет данные курса по указанному ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Курс успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    public ResponseEntity<Course> updateCourse(
            @PathVariable @Parameter(description = "ID курса", example = "1") Long id,
            @RequestBody @Parameter(description = "Новые данные курса") Course courseDetails) {
        Course updatedCourse = courseService.updateCourse(id, courseDetails);
        return ResponseEntity.ok(updatedCourse);
    }

    @GetMapping("/{courseId}/students")
    @Operation(summary = "Получить студентов курса", description = "Возвращает список студентов для указанного курса")
    @ApiResponse(responseCode = "200", description = "Список студентов")
    public List<Student> getStudentsByCourse(
            @PathVariable @Parameter(description = "ID курса", example = "1") Long courseId) {
        Course course = courseService.getCourseById(courseId);
        return studentService.getStudentsByCourse(course);
    }

    @PutMapping("/students/{studentId}")
    @Operation(summary = "Обновить данные студента", description = "Обновляет данные студента по указанному ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Студент успешно обновлен"),
            @ApiResponse(responseCode = "404", description = "Студент не найден")
    })
    public ResponseEntity<Student> updateStudent(
            @PathVariable @Parameter(description = "ID студента", example = "1") Long studentId,
            @RequestBody @Parameter(description = "Новые данные студента") Student studentDetails) {
        Student updatedStudent = studentService.updateStudent(studentId, studentDetails);
        return ResponseEntity.ok(updatedStudent);
    }

    @PostMapping("/students")
    @Operation(summary = "Создать нового студента", description = "Создает нового студента с переданными данными")
    @ApiResponse(responseCode = "200", description = "Студент успешно создан")
    public Student createStudent(@RequestBody @Parameter(description = "Данные студента") Student student) {
        return studentService.createStudent(student);
    }

    @GetMapping("/{courseId}/students-by-last-name-starts-with/{letter}")
    @Operation(summary = "Фильтр студентов по фамилии",
            description = "Возвращает студентов курса, чьи фамилии начинаются с указанной буквы")
    @ApiResponse(responseCode = "200", description = "Список отфильтрованных студентов")
    public List<Student> getStudentsByCourseAndLastNameStartsWith(
            @PathVariable @Parameter(description = "ID курса", example = "1") Long courseId,
            @PathVariable @Parameter(description = "Первая буква фамилии", example = "A") char letter) {
        Course course = courseService.getCourseById(courseId);
        return studentService.findStudentsByCourseAndLastNameStartsWith(course, letter);
    }

    // Остальные методы аналогично...
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
//
GET http://localhost:8080/api/courses/2/students-by-last-name-starts-with/B
GET http://localhost:8080/api/courses/2/students-by-last-name-starts-with/%D0%91
 */