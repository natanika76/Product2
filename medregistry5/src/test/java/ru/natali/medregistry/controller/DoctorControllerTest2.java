package ru.natali.medregistry.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.natali.medregistry.dto.DoctorDTO;
import ru.natali.medregistry.exceptions.DoctorNotFoundException;
import ru.natali.medregistry.service.DoctorService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//С аннотацией @SpringBootTest
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DoctorControllerTest2 {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DoctorService doctorService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    public void getAllDoctors_ShouldReturnAllDoctors() throws Exception {
        // Arrange
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setId(1L);
        doctorDTO.setFirstName("Иван");
        doctorDTO.setLastName("Петров");
        doctorDTO.setSpecialization("Хирург");

        List<DoctorDTO> doctors = List.of(doctorDTO);
        when(doctorService.getAllDoctors()).thenReturn(doctors);

        // Act & Assert
        mockMvc.perform(get("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].firstName").value("Иван"))
                .andExpect(jsonPath("$[0].lastName").value("Петров"))
                .andExpect(jsonPath("$[0].specialization").value("Хирург"));
    }

    @Test
    public void getDoctorById_WhenDoctorExists_ShouldReturnDoctor() throws Exception {
        // Arrange
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setId(1L);
        doctorDTO.setFirstName("Иван");
        doctorDTO.setLastName("Петров");
        doctorDTO.setSpecialization("Хирург");

        when(doctorService.getDoctorById(1L)).thenReturn(doctorDTO);

        // Act & Assert
        mockMvc.perform(get("/api/doctors/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("Иван"))
                .andExpect(jsonPath("$.lastName").value("Петров"))
                .andExpect(jsonPath("$.specialization").value("Хирург"));
    }

    @Test
    public void getDoctorById_WhenDoctorNotExists_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(doctorService.getDoctorById(1L)).thenThrow(new EntityNotFoundException("Doctor not found with id: 1"));

        // Act & Assert
        mockMvc.perform(get("/api/doctors/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getDoctorById_WhenDoctorNotFound_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(doctorService.getDoctorById(1L))
                .thenThrow(new EntityNotFoundException("Doctor not found with id: 1"));

        // Act & Assert
        mockMvc.perform(get("/api/doctors/1"))
                .andExpect(status().isNotFound());

        verify(doctorService, times(1)).getDoctorById(1L);
    }

    @Test
    public void getDoctorById_ShouldReturnDoctor() throws Exception {
        // Arrange
        DoctorDTO doctorDto = new DoctorDTO(1L, "John", "Doe", "Cardiology");

        when(doctorService.getDoctorById(1L)).thenReturn(doctorDto);

        // Act & Assert
        mockMvc.perform(get("/api/doctors/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.specialization").value("Cardiology"));

        verify(doctorService, times(1)).getDoctorById(1L);
    }
}
