package ru.natali.students.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.natali.students.exception.ResourceNotFoundException;
import ru.natali.students.model.Student;
import ru.natali.students.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Метод для получения всех студентов
    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    // Метод для получения одного студента по ID
    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable("id") Long id) {
        return studentService.findById(id)
                //.orElseThrow(() -> new ResourceNotFoundException("Студент с id " + id + " не найден"));
                .orElseThrow(() -> new ResourceNotFoundException("Student with id " + id + " not found"));
    }

    // Метод для добавления нового студента
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student addNewStudent(@RequestBody Student newStudent) {
        return studentService.save(newStudent);
    }

    // Метод для обновления информации о студенте
    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable("id") Long id, @RequestBody Student updatedStudent) {
        if (studentService.findById(id).isEmpty()) {
           // throw new ResourceNotFoundException("Студент с id " + id + " не найден");
            throw new ResourceNotFoundException("Student with id " + id + " not found");
        }
        updatedStudent.setId(id);
        return studentService.update(updatedStudent);
    }

    // Метод для удаления студента по ID
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteStudent(@PathVariable("id") Long id) {
        studentService.deleteById(id);
    }

    // Добавляем метод обработки исключений
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}

