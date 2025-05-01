package ru.natali.courses.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.natali.courses.dto.CourseBasicDTO;
import ru.natali.courses.model.Course;
import ru.natali.courses.service.CourseService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourseService courseService;

    @Test
    public void getAllCourses_ShouldReturnListOfCourses() throws Exception {
        // Arrange
        Course course1 = new Course();
        course1.setId(1L);
        course1.setName("Java Programming");
        course1.setStartDate(LocalDate.of(2023, 1, 1));
        course1.setActive(true);
        course1.setArchived(false);

        Course course2 = new Course();
        course2.setId(2L);
        course2.setName("Spring Framework");
        course2.setStartDate(LocalDate.of(2023, 2, 1));
        course2.setActive(true);
        course2.setArchived(false);

        List<Course> mockCourses = Arrays.asList(course1, course2);

        when(courseService.getAllCourses()).thenReturn(mockCourses);

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Java Programming"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].startDate").value("2023-01-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].active").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].archived").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].studentCount").value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Spring Framework"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].startDate").value("2023-02-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].active").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].archived").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].studentCount").value(0));
    }

    @Test
    public void getAllCourses_WhenNoCourses_ShouldReturnEmptyList() throws Exception {
        // Arrange
        when(courseService.getAllCourses()).thenReturn(List.of());

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }
    @Test
    public void getActiveCourses_ShouldReturnActiveCourses() throws Exception {
        // Подготовка данных
        Course activeCourse1 = new Course();
        activeCourse1.setId(1L);
        activeCourse1.setName("Активный курс 1");
        activeCourse1.setStartDate(LocalDate.of(2023, 1, 1));
        activeCourse1.setActive(true);

        Course activeCourse2 = new Course();
        activeCourse2.setId(2L);
        activeCourse2.setName("Активный курс 2");
        activeCourse2.setStartDate(LocalDate.of(2023, 2, 1));
        activeCourse2.setActive(true);

        List<CourseBasicDTO> mockActiveCourses = Arrays.asList(
                new CourseBasicDTO(activeCourse1),
                new CourseBasicDTO(activeCourse2)
        );

        // Мокируем сервис
        when(courseService.getActiveCourses()).thenReturn(mockActiveCourses);

        // Выполняем запрос и проверяем результат
        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses/active"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Активный курс 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].startDate").value("2023-01-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].active").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Активный курс 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].startDate").value("2023-02-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].active").value(true));
    }
    @Test
    public void getActiveCourses_WhenNoActiveCourses_ShouldReturnEmptyList() throws Exception {
        // Мокируем сервис с пустым списком
        when(courseService.getActiveCourses()).thenReturn(List.of());

        // Проверяем пустой ответ
        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses/active"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    public void getArchivedCourses_ShouldReturnArchivedCourses() throws Exception {
        // Подготовка данных
        Course archivedCourse1 = new Course();
        archivedCourse1.setId(3L);
        archivedCourse1.setName("Архивный курс 1");
        archivedCourse1.setStartDate(LocalDate.of(2022, 1, 1));
        archivedCourse1.setActive(false);

        Course archivedCourse2 = new Course();
        archivedCourse2.setId(4L);
        archivedCourse2.setName("Архивный курс 2");
        archivedCourse2.setStartDate(LocalDate.of(2022, 2, 1));
        archivedCourse2.setActive(false);

        List<Course> mockArchivedCourses = Arrays.asList(archivedCourse1, archivedCourse2);

        // Мокируем сервис
        when(courseService.getArchivedCourses()).thenReturn(mockArchivedCourses);

        // Выполняем запрос и проверяем результат
        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses/archived"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Архивный курс 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].startDate").value("2022-01-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].active").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(4))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("Архивный курс 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].startDate").value("2022-02-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].active").value(false));
    }

    @Test
    public void getArchivedCourses_WhenNoArchivedCourses_ShouldReturnEmptyList() throws Exception {
        // Мокируем сервис с пустым списком
        when(courseService.getArchivedCourses()).thenReturn(List.of());

        // Проверяем пустой ответ
        mockMvc.perform(MockMvcRequestBuilders.get("/api/courses/archived"))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isArray())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

}
