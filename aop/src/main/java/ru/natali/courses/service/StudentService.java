package ru.natali.courses.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.natali.courses.model.Course;
import ru.natali.courses.model.Student;
import ru.natali.courses.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    @Autowired
    private StudentRepository studentRepository;

    public List<Student> getStudentsByCourse(Course course) {
        return studentRepository.findByCourse(course);
    }
    // Метод для создания студента
    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student updateStudent(Long studentId, Student studentDetails) {
        Optional<Student> optionalStudent = studentRepository.findById(studentId);
        if (optionalStudent.isPresent()) {
            Student existingStudent = optionalStudent.get();
            existingStudent.setFirstName(studentDetails.getFirstName());
            existingStudent.setLastName(studentDetails.getLastName());
            existingStudent.setCourse(studentDetails.getCourse()); // Добавляем привязку к курсу
            return studentRepository.save(existingStudent);
        } else {
            throw new RuntimeException("Студент с ID " + studentId + " не найден");
        }
    }
}
