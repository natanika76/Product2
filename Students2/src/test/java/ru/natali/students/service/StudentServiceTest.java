package ru.natali.students.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.natali.students.model.Student;
import ru.natali.students.repository.StudentRepositoryImpl;

class StudentServiceTest {

    @InjectMocks
    private StudentService studentService;

    @Mock
    private StudentRepositoryImpl studentRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllStudents() {
        // Arrange
        List<Student> expectedStudents = Arrays.asList(
                new Student(1L, "Иван Иванов", "ivan@example.com", Arrays.asList("Математика", "Физика")),
                new Student(2L, "Петр Петров", "petr@example.com", Arrays.asList("История", "География"))
        );
        when(studentRepository.findAll()).thenReturn(expectedStudents);

        // Act
        List<Student> actualStudents = studentService.getAllStudents();

        // Assert
        assertEquals(expectedStudents, actualStudents);
    }

    @Test
    void testFindById_Found() {
        // Arrange
        Student expectedStudent = new Student(1L, "Иван Иванов", "ivan@example.com", Arrays.asList("Математика", "Физика"));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(expectedStudent));

        // Act
        Optional<Student> actualOptionalStudent = studentService.findById(1L);

        // Assert
        assertTrue(actualOptionalStudent.isPresent());
        assertEquals(expectedStudent, actualOptionalStudent.get());
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Optional<Student> actualOptionalStudent = studentService.findById(999L);

        // Assert
        assertFalse(actualOptionalStudent.isPresent());
    }

    @Test
    void testDeleteById() {
        // Arrange
        long idToDelete = 123L;

        // Act
        studentService.deleteById(idToDelete);

        // Assert
        verify(studentRepository, times(1)).deleteById(idToDelete);
    }
}