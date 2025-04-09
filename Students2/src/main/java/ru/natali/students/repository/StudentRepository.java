package ru.natali.students.repository;

import ru.natali.students.model.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository {
    List<Student> findAll();
    Optional<Student> findById(Long id);
    Student save(Student student);
    int update(Student student);
    void deleteById(Long id);
}

