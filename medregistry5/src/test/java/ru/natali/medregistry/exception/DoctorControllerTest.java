package ru.natali.medregistry.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.natali.medregistry.exceptions.DoctorNotFoundException;
import ru.natali.medregistry.service.DoctorService;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoctorService doctorService;

    @Test
    void getDoctorById_NonExistentId_ReturnsNotFound() throws Exception {
        // Устанавливаем поведение мока: сервис бросает исключение
        when(doctorService.getDoctorById(999L))
                .thenThrow(new DoctorNotFoundException(999L));

        // Отправляем GET-запрос и проверяем ответ
        mockMvc.perform(get("/api/doctors/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Doctor not found with id: 999"));
    }
    @Test
    void deleteDoctor_NonExistentId_ReturnsNotFound() throws Exception {
        doThrow(new DoctorNotFoundException(999L))
                .when(doctorService).deleteDoctor(999L);

        mockMvc.perform(delete("/api/doctors/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Doctor not found with id: 999"));
    }
}
