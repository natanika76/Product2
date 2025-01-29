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
import ru.natali.students.repository.StudentRepository;

class StudentServiceTest {

    @InjectMocks
    private StudentService studentService;

    @Mock
    private StudentRepository studentRepository;

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
    void testSave() {
        // Arrange
        Student newStudent = new Student(null, "Новый Студент", "new_student@example.com", Arrays.asList("Русский язык", "Литература"));
        when(studentRepository.save(newStudent)).thenReturn(newStudent);

        // Act
        Student savedStudent = studentService.save(newStudent);

        // Assert
        assertEquals(newStudent.getFullName(), savedStudent.getFullName());
        assertEquals(newStudent.getEmail(), savedStudent.getEmail());
        assertEquals(newStudent.getCourses(), savedStudent.getCourses());
    }

    @Test
    void testUpdate_ExistingStudent() {
        // Arrange
        Student existingStudent = new Student(1L, "Иван Иванов", "ivan@example.com", Arrays.asList("Математика", "Физика"));
        Student updatedStudent = new Student(1L, "Иван Иванович", "ivan.ivanovich@example.com", Arrays.asList("Информатика"));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.update(updatedStudent)).thenReturn(1);

        // Act
        Student returnedStudent = studentService.update(updatedStudent);

        // Assert
        assertEquals(updatedStudent.getId(), returnedStudent.getId());
        assertEquals(updatedStudent.getFullName(), returnedStudent.getFullName());
        assertEquals(updatedStudent.getEmail(), returnedStudent.getEmail());
        assertEquals(updatedStudent.getCourses(), returnedStudent.getCourses());
    }

    @Test
    void testUpdate_NonExistingStudent() {
        // Arrange
        Student updatedStudent = new Student(999L, "Неизвестный Студент", "unknown@example.com", Arrays.asList("Наука"));
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        try {
            studentService.update(updatedStudent);
            fail("Expected exception to be thrown");
        } catch (RuntimeException e) {
            assertEquals("Студент с id 999 не содержится в базе данных.", e.getMessage());
        }
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