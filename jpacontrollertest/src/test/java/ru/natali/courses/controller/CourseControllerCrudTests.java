package ru.natali.courses.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.natali.courses.dto.CourseBasicDTO;
import ru.natali.courses.model.Course;
import ru.natali.courses.service.CourseService;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class CourseControllerCrudTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourseService courseService;

    @Test
    public void createCourse_ShouldReturnCreatedCourse() throws Exception {
        // Подготовка тестовых данных
        Course newCourse = new Course();
        newCourse.setName("Новый курс");
        newCourse.setStartDate(LocalDate.of(2024, 1, 1));
        newCourse.setActive(true);
        newCourse.setArchived(false);

        Course savedCourse = new Course();
        savedCourse.setId(1L);
        savedCourse.setName(newCourse.getName());
        savedCourse.setStartDate(newCourse.getStartDate());
        savedCourse.setActive(newCourse.isActive());
        savedCourse.setArchived(newCourse.isArchived());

        // Мокируем сервис
        when(courseService.createCourse(any(Course.class))).thenReturn(savedCourse);

        // Выполняем запрос и проверяем результат
        mockMvc.perform(MockMvcRequestBuilders.post("/api/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newCourse)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Новый курс"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate").value("2024-01-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.active").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.archived").value(false));
    }

    @Test
    public void deleteCourse_ShouldPerformSoftDelete() throws Exception {
        // Подготовка тестовых данных
        Long courseId = 8L;
        Course courseToDelete = new Course();
        courseToDelete.setId(courseId);
        courseToDelete.setActive(true);

        // Мокируем сервис
        doNothing().when(courseService).deleteCourse(courseId);

        // Выполняем запрос и проверяем результат
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/courses/" + courseId))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Проверяем, что метод сервиса был вызван
        verify(courseService, times(1)).deleteCourse(courseId);
    }

    @Test
    public void updateCourse_ShouldReturnUpdatedCourse() throws Exception {
        // Подготовка тестовых данных
        Long courseId = 2L;
        CourseBasicDTO updateDto = new CourseBasicDTO();
        updateDto.setName("New Updated Course");
        updateDto.setStartDate(LocalDate.of(2023, 11, 1));
        updateDto.setActive(false);

        Course updatedCourse = new Course();
        updatedCourse.setId(courseId);
        updatedCourse.setName(updateDto.getName());
        updatedCourse.setStartDate(updateDto.getStartDate());
        updatedCourse.setActive(updateDto.isActive());

        // Мокируем сервис
        when(courseService.updateCourse(eq(courseId), any(CourseBasicDTO.class))).thenReturn(updatedCourse);

        // Выполняем запрос и проверяем результат
        mockMvc.perform(MockMvcRequestBuilders.put("/api/courses/" + courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(courseId))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("New Updated Course"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.startDate").value("2023-11-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.active").value(false));
    }

    @Test
    public void updateCourse_WithNonExistingId_ShouldReturnNotFound() throws Exception {
        // Подготовка тестовых данных
        Long nonExistingId = 999L;
        CourseBasicDTO updateDto = new CourseBasicDTO();
        updateDto.setName("Несуществующий курс");
        updateDto.setStartDate(LocalDate.now());
        updateDto.setActive(true);

        // Мокируем сервис для выброса исключения
        when(courseService.updateCourse(eq(nonExistingId), any(CourseBasicDTO.class)))
                .thenThrow(new RuntimeException("Курс с ID " + nonExistingId + " не найден"));

        // Выполняем запрос и проверяем результат
        mockMvc.perform(MockMvcRequestBuilders.put("/api/courses/" + nonExistingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andDo(print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Курс с ID 999 не найден"));
    }
}
