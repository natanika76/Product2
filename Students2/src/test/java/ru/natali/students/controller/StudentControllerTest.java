package ru.natali.students.controller;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.natali.students.dto.StudentRegistrationDto;
import ru.natali.students.model.Student;
import ru.natali.students.service.StudentService;

class StudentControllerTest {

    private MockMvc mockMvc;
    private StudentService studentServiceMock;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        studentServiceMock = mock(StudentService.class);
        StudentController controller = new StudentController(studentServiceMock);
        mockMvc = standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetAllStudents() throws Exception {
        // Arrange
        List<Student> students = Arrays.asList(
                new Student(1L, "Иван Иванов", "ivan@example.com", Arrays.asList("Математика", "Физика"))
        );

        when(studentServiceMock.getAllStudents()).thenReturn(students);

        // Act
        MvcResult result = mockMvc.perform(get("/api/students")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        String responseContent = result.getResponse().getContentAsString();
        List<Student> actualStudents = Arrays.asList(objectMapper.readValue(responseContent, Student[].class));

        assertThat(actualStudents, hasSize(1));
        assertThat(actualStudents.get(0).getFullName(), is("Иван Иванов"));
        assertThat(actualStudents.get(0).getCourses(), containsInAnyOrder("Математика", "Физика"));
    }
    @Test
    void testGetStudentById_Found() throws Exception {
        // Arrange
        long id = 1L;
        Student expectedStudent = new Student(id, "Иван Иванов", "ivan@example.com", Arrays.asList("Математика", "Физика"));

        when(studentServiceMock.findById(id)).thenReturn(Optional.of(expectedStudent));

        // Act
        MvcResult result = mockMvc.perform(get("/api/students/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        String responseContent = result.getResponse().getContentAsString();
        Student actualStudent = objectMapper.readValue(responseContent, Student.class);

        assertThat(actualStudent.getId(), is(id));
        assertThat(actualStudent.getFullName(), is("Иван Иванов"));
        assertThat(actualStudent.getCourses(), containsInAnyOrder("Математика", "Физика"));
    }

    @Test
    void testGetStudentById_NotFound() throws Exception {
        // Arrange
        long id = 999L;

        when(studentServiceMock.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/students/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void testAddNewStudent() throws Exception {
        // Arrange
        Student newStudent = new Student(null, "Алексей Алексеев", "alexey@example.com", Arrays.asList("C++", "Java"));
        Student savedStudent = new Student(10L, "Алексей Алексеев", "alexey@example.com", Arrays.asList("C++", "Java"));

        when(studentServiceMock.save(any())).thenReturn(savedStudent);

        // Act
        MvcResult result = mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStudent)))
                .andExpect(status().isCreated())
                .andReturn();

        // Assert
        String responseContent = result.getResponse().getContentAsString();
        Student actualStudent = objectMapper.readValue(responseContent, Student.class);

        assertThat(actualStudent.getId(), is(10L));
        assertThat(actualStudent.getFullName(), is("Алексей Алексеев"));
        assertThat(actualStudent.getCourses(), containsInAnyOrder("C++", "Java"));
    }
    @Test
    void testRegisterStudent() throws Exception {
        // Arrange
        StudentRegistrationDto registrationDto = new StudentRegistrationDto("Иван Иванов", "ivan@example.com");
        Student expectedStudent = new Student(1L, "Иван Иванов", "ivan@example.com", Collections.singletonList("Математика"));

        when(studentServiceMock.save(any()))
                .thenAnswer(invocationOnMock -> {
                    Student student = invocationOnMock.getArgument(0);
                    student.setId(1L);
                    student.getCourses().add("Математика"); // добавляем курс автоматически
                    return student;
                });

        // Act
        MvcResult result = mockMvc.perform(post("/api/students/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registrationDto)))
                .andExpect(status().isCreated())
                .andReturn();

        // Assert
        String responseContent = result.getResponse().getContentAsString();
        Student actualStudent = objectMapper.readValue(responseContent, Student.class);

        assertEquals(1L, actualStudent.getId());
        assertEquals("Иван Иванов", actualStudent.getFullName());
        assertTrue(actualStudent.getCourses().contains("Математика")); // проверяем, что курс был добавлен
    }

    @Test
    void testAddCourseToStudent() throws Exception {
        // Arrange
        long id = 1L;
        String courseName = "Физика";
        Student existingStudent = new Student(id, "Иван Иванов", "ivan@example.com", Collections.singletonList("Математика"));
        Student updatedStudent = new Student(id, "Иван Иванов", "ivan@example.com", Arrays.asList("Математика", "Физика"));

        when(studentServiceMock.findById(id)).thenReturn(Optional.of(existingStudent));
        when(studentServiceMock.update(any())).thenReturn(updatedStudent);

        // Act
        MvcResult result = mockMvc.perform(patch("/api/students/{id}/add-course/{courseName}", id, courseName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // Assert
        String responseContent = result.getResponse().getContentAsString();
        Student actualStudent = objectMapper.readValue(responseContent, Student.class);

        assertThat(actualStudent.getId(), is(id));
        assertThat(actualStudent.getFullName(), is("Иван Иванов"));
        assertThat(actualStudent.getCourses(), containsInAnyOrder("Математика", "Физика"));
    }
}