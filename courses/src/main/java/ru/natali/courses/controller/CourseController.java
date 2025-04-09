package ru.natali.courses.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.natali.courses.model.Course;
import ru.natali.courses.service.CourseService;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

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
        Course updatedCourse = courseService.updateCourse(courseDetails);
        return ResponseEntity.ok(updatedCourse);
    }
}
/*
GET http://localhost:8081/api/courses
DELETE http://localhost:8081/api/courses/3
GET http://localhost:8081/api/courses/active
GET http://localhost:8081/api/courses/archived
POST http://localhost:8081/api/courses
{
  "name": "Example Course",
  "startDate": "2023-10-15",
  "active": true
}
PUT http://localhost:8081/api/courses/2
{
  "name": "Updated Example Course",
  "startDate": "2023-11-01",
  "active": false
}
 */