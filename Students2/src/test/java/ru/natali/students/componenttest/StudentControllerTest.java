package ru.natali.students.componenttest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.natali.students.controller.StudentController;
import ru.natali.students.model.Student;
import ru.natali.students.service.StudentService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class StudentControllerTest {

    private MockMvc mockMvc;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(studentController).build();
    }

    @Test
    public void testGetAllStudents() throws Exception {
        Student student1 = new Student(1L, "Иван Иванов", "ivan@example.com", Arrays.asList("Математика", "Физика"));
        Student student2 = new Student(2L, "Петр Петров", "petr@example.com", Arrays.asList("Программирование", "История"));
        List<Student> students = Arrays.asList(student1, student2);

        when(studentService.getAllStudents()).thenReturn(students);

        mockMvc.perform(get("/api/students")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].fullName").value("Иван Иванов"))
                .andExpect(jsonPath("$[1].fullName").value("Петр Петров"));
    }

    @Test
    public void testGetStudentById() throws Exception {
        Student student = new Student(1L, "Иван Иванов", "ivan@example.com", Arrays.asList("Математика", "Физика"));

        when(studentService.findById(1L)).thenReturn(Optional.of(student));

        mockMvc.perform(get("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Иван Иванов"))
                .andExpect(jsonPath("$.email").value("ivan@example.com"));
    }

    @Test
    public void testAddNewStudent() throws Exception {
        Student newStudent = new Student(null, "Сергей Сергеев", "sergey@example.com", Arrays.asList("Химия", "Биология"));
        Student savedStudent = new Student(3L, "Сергей Сергеев", "sergey@example.com", Arrays.asList("Химия", "Биология"));

        when(studentService.save(any(Student.class))).thenReturn(savedStudent);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\":\"Сергей Сергеев\",\"email\":\"sergey@example.com\",\"courses\":[\"Химия\",\"Биология\"]}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3L))
                .andExpect(jsonPath("$.fullName").value("Сергей Сергеев"));
    }

    @Test
    public void testUpdateStudent() throws Exception {
        Student updatedStudent = new Student(1L, "Иван Иванов", "ivan@example.com", Arrays.asList("Математика", "Физика", "Химия"));

        when(studentService.findById(1L)).thenReturn(Optional.of(updatedStudent));
        when(studentService.update(any(Student.class))).thenReturn(updatedStudent);

        mockMvc.perform(put("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"fullName\":\"Иван Иванов\",\"email\":\"ivan@example.com\",\"courses\":[\"Математика\",\"Физика\",\"Химия\"]}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.courses.length()").value(3));
    }

    @Test
    public void testDeleteStudent() throws Exception {
        doNothing().when(studentService).deleteById(1L);

        mockMvc.perform(delete("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(studentService, times(1)).deleteById(1L);
    }

    @Test
    public void testStudentNotFound() throws Exception {
        when(studentService.findById(999L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/students/999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Student with id 999 not found"));
    }
}