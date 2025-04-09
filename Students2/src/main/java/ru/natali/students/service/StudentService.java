package ru.natali.students.service;

import org.springframework.stereotype.Service;
import ru.natali.students.model.Student;
import ru.natali.students.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // Получение списка всех студентов
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Поиск студента по ID
    public Optional<Student> findById(Long id) {
        return studentRepository.findById(id);
    }

    // Удаление студента по ID
    public void deleteById(Long id) {
        studentRepository.deleteById(id);
    }

    // Сохранение нового студента
    public Student save(Student student) {
        return studentRepository.save(student);
    }

    // Обновление информации о студенте
    public Student update(Student student) {
        if (studentRepository.findById(student.getId()).isPresent()) {
            return studentRepository.save(student);
        } else {
            throw new RuntimeException("Студент с таким ID не найден.");
        }
    }
}