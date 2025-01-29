package ru.natali.students.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.natali.students.model.Student;

class StudentRepositoryTest {

    @InjectMocks
    private StudentRepository repository;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        // Arrange
        List<Student> expectedStudents = createSampleStudents();
        given(jdbcTemplate.query(eq("SELECT * FROM students"), any(RowMapper.class))).willReturn(expectedStudents);

        // Act
        List<Student> actualStudents = repository.findAll();

        // Assert
        assertEquals(expectedStudents.size(), actualStudents.size());
        assertEquals(expectedStudents.get(0).getId(), actualStudents.get(0).getId());
        assertEquals(expectedStudents.get(0).getFullName(), actualStudents.get(0).getFullName());
        assertEquals(expectedStudents.get(0).getEmail(), actualStudents.get(0).getEmail());
        assertEquals(expectedStudents.get(0).getCourses(), actualStudents.get(0).getCourses());
    }

    @Test
    void testFindById_Found() {
        // Arrange
        Student expectedStudent = createSampleStudents().get(0);
        given(jdbcTemplate.query(eq("SELECT * FROM students WHERE id = ?"),
                any(RowMapper.class),
                eq(expectedStudent.getId())))
                .willReturn(List.of(expectedStudent));

        // Act
        Optional<Student> optionalStudent = repository.findById(expectedStudent.getId());

        // Assert
        assertTrue(optionalStudent.isPresent());
        assertEquals(expectedStudent.getId(), optionalStudent.get().getId());
        assertEquals(expectedStudent.getFullName(), optionalStudent.get().getFullName());
        assertEquals(expectedStudent.getEmail(), optionalStudent.get().getEmail());
        assertEquals(expectedStudent.getCourses(), optionalStudent.get().getCourses());
    }

    @Test
    void testFindById_NotFound() {
        // Arrange
        long nonExistentId = 999L;
        given(jdbcTemplate.query(eq("SELECT * FROM students WHERE id = ?"),
                any(RowMapper.class),
                eq(nonExistentId)))
                .willReturn(new ArrayList<>());

        // Act
        Optional<Student> optionalStudent = repository.findById(nonExistentId);

        // Assert
        assertTrue(optionalStudent.isEmpty());
    }

    @Test
    void testSave() {
        // Arrange
        Student newStudent = new Student(null, "Новый Студент", "new_student@example.com", List.of("Русский язык", "Литература"));
        given(jdbcTemplate.update(any(String.class), any(Object[].class))).willReturn(1);

        // Act
        Student savedStudent = repository.save(newStudent);

        // Assert
        assertEquals(newStudent.getFullName(), savedStudent.getFullName());
        assertEquals(newStudent.getEmail(), savedStudent.getEmail());
        assertEquals(newStudent.getCourses(), savedStudent.getCourses());
    }

    @Test
    void testUpdate() {
        // Arrange
        Student existingStudent = new Student(1L, "Иван Иванов", "ivan@example.com", List.of("Математика", "Физика"));
        given(jdbcTemplate.update(any(String.class), any(Object[].class))).willReturn(1);

        // Act
        int rowsUpdated = repository.update(existingStudent);

        // Assert
        assertEquals(1, rowsUpdated);
    }

    @Test
    void testDeleteById() {
        // Arrange
        long idToDelete = 123L;

        // Act
        repository.deleteById(idToDelete);

        // Assert
        verify(jdbcTemplate, times(1)).update(eq("DELETE FROM students WHERE id = ?"), eq(idToDelete));
    }

    private List<Student> createSampleStudents() {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1L, "Иван Иванов", "ivan@example.com", List.of("Математика", "Физика")));
        students.add(new Student(2L, "Петр Петров", "petr@example.com", List.of("История", "География")));
        return students;
    }
}