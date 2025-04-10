package ru.natali.courses.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.natali.courses.model.Course;
import ru.natali.courses.model.Student;
import ru.natali.courses.service.CourseService;
import ru.natali.courses.service.StudentService;

import java.util.List;

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
}
/*
GET http://localhost:8081/api/courses
DELETE http://localhost:8081/api/courses/3  //удаляет в архив, просто ставит значение false
GET http://localhost:8081/api/courses/active
GET http://localhost:8081/api/courses/archived
POST http://localhost:8081/api/courses
{
  "name": "New Example Course",
  "startDate": "2025-12-05",
  "active": true
}
PUT http://localhost:8081/api/courses/2
{
  "name": "Updated Example Course",
  "startDate": "2023-11-01",
  "active": false
}
 */