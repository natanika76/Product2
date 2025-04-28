package ru.natali.courses.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.natali.courses.model.Course;
import ru.natali.courses.model.Review;
import ru.natali.courses.model.Role;
import ru.natali.courses.model.Student;
import ru.natali.courses.repository.StudentRepository;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private StudentService studentService;

    private Student testStudent;
    private Course testCourse;
    private Review testReview;

    @BeforeEach
    void setUp() {
        // Инициализация Course с корректными данными
        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setName("Computer Science");

        // Задание даты старта курса
        testCourse.setStartDate(LocalDate.of(2024, 10, 12)); // Локальная дата без парсинга строк

        testCourse.setActive(true);
        testCourse.setArchived(false);
        testCourse.setReviews(new ArrayList<>());
        testCourse.setStudents(new HashSet<>());

        // Инициализация Student
        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setFirstName("John");
        testStudent.setLastName("Doe");
        testStudent.setEmail("john.doe@example.com");
        testStudent.setUsername("john_doe");
        testStudent.setPassword("encodedPassword");
        testStudent.setRole(Role.ADMIN);
        testStudent.setCourse(testCourse);
        testStudent.setEnrolledCourses(new HashSet<>(Collections.singleton(testCourse)));
        testStudent.setReviews(new ArrayList<>());

        // Инициализация Review
        testReview = new Review();
        testReview.setId(1L);
        testReview.setReviewText("Great course!");
        testReview.setRating(5);
        testReview.setCreatedAt(LocalDate.now().atStartOfDay()); // Использование локальной даты
        testReview.setStudent(testStudent);
        testReview.setCourse(testCourse);

        // Установка связей
        testStudent.getReviews().add(testReview);
        testCourse.getReviews().add(testReview);
        testCourse.getStudents().add(testStudent);
    }

    @Test
    void checkPassword_ShouldReturnTrue_WhenPasswordMatches() {
        when(passwordEncoder.matches("rawPassword", "encodedPassword")).thenReturn(true);
        assertTrue(studentService.checkPassword("rawPassword", "encodedPassword"));
        verify(passwordEncoder).matches("rawPassword", "encodedPassword");
    }

    @Test
    void getStudentsByCourse_ShouldReturnStudentsList() {
        when(studentRepository.findByCourse(testCourse)).thenReturn(List.of(testStudent));
        List<Student> students = studentService.getStudentsByCourse(testCourse);

        assertEquals(1, students.size());
        assertEquals("John", students.get(0).getFirstName());
        assertEquals(1, students.get(0).getReviews().size());
        assertEquals("Great course!", students.get(0).getReviews().get(0).getReviewText());
    }

    @Test
    void createStudent_ShouldSaveStudent() {
        Student newStudent = new Student();
        newStudent.setFirstName("Alice");
        newStudent.setLastName("Smith");
        newStudent.setEmail("alice@example.com");
        newStudent.setUsername("alice_smith");
        newStudent.setPassword("password123");
        newStudent.setRole(Role.USER);

        when(studentRepository.save(any(Student.class))).thenReturn(newStudent);

        Student savedStudent = studentService.createStudent(newStudent);
        assertNotNull(savedStudent);
        assertEquals("Alice", savedStudent.getFirstName());
        assertEquals(Role.USER, savedStudent.getRole());
    }

    @Test
    void findStudentsByCourseAndLastNameStartsWith_ShouldReturnFilteredList() {
        when(studentRepository.findByCourseAndLastNameStartingWithIgnoreCase(testCourse, "D"))
                .thenReturn(List.of(testStudent));

        List<Student> students = studentService.findStudentsByCourseAndLastNameStartsWith(testCourse, 'D');
        assertEquals(1, students.size());
        assertEquals("Doe", students.get(0).getLastName());
    }

    @Test
    void loadUserByUsername_ShouldReturnUserDetails() {
        when(studentRepository.findByUsername("john_doe")).thenReturn(Optional.of(testStudent));

        UserDetails userDetails = studentService.loadUserByUsername("john_doe");
        assertEquals("john_doe", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertEquals("ADMIN", userDetails.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    void loadUserByUsername_ShouldThrowException_WhenUserNotFound() {
        when(studentRepository.findByUsername("unknown")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () ->
                studentService.loadUserByUsername("unknown")
        );
    }

    @Test
    void findByUsername_ShouldReturnStudent_WhenExists() {
        when(studentRepository.findByUsername("john_doe")).thenReturn(Optional.of(testStudent));
        Student foundStudent = studentService.findByUsername("john_doe");

        assertNotNull(foundStudent);
        assertEquals("john.doe@example.com", foundStudent.getEmail());
        assertEquals(1, foundStudent.getEnrolledCourses().size());
        assertEquals("Computer Science",
                foundStudent.getEnrolledCourses().iterator().next().getName());
    }

    @Test
    void findByUsername_ShouldReturnNull_WhenNotExists() {
        when(studentRepository.findByUsername("unknown")).thenReturn(Optional.empty());
        assertNull(studentService.findByUsername("unknown"));
    }

    @Test
    void updateStudent_ShouldUpdateExistingStudent() {
        // Arrange
        long studentId = 1L;
        Student updatedStudentDetails = new Student();
        updatedStudentDetails.setFirstName("Jane");
        updatedStudentDetails.setLastName("Doe");
        updatedStudentDetails.setCourse(testCourse);

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(testStudent));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Student updatedStudent = studentService.updateStudent(studentId, updatedStudentDetails);

        // Assert
        assertEquals(updatedStudentDetails.getFirstName(), updatedStudent.getFirstName());
        assertEquals(updatedStudentDetails.getLastName(), updatedStudent.getLastName());
        assertSame(testCourse, updatedStudent.getCourse());
        verify(studentRepository).findById(studentId);
        verify(studentRepository).save(any(Student.class));
    }

    // Дополнительно проверим случай, когда запрашиваемого студента не существует
    @Test
    void updateStudent_ShouldThrowRuntimeException_WhenStudentNotFound() {
        long nonExistentStudentId = 999L;
        Student updatedStudentDetails = new Student();
        updatedStudentDetails.setFirstName("Nonexistent");
        updatedStudentDetails.setLastName("User");

        when(studentRepository.findById(nonExistentStudentId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            studentService.updateStudent(nonExistentStudentId, updatedStudentDetails);
        });

        assertEquals("Студент с ID " + nonExistentStudentId + " не найден", exception.getMessage());
        verify(studentRepository).findById(nonExistentStudentId);
    }
}