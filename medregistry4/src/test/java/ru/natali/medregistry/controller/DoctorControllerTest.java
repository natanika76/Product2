package ru.natali.medregistry.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.natali.medregistry.dto.DoctorDTO;
import ru.natali.medregistry.exceptions.DoctorNotFoundException;
import ru.natali.medregistry.service.DoctorService;

import java.util.List;

//С аннотацией @WebMvcTest
@WebMvcTest(DoctorController.class)
@AutoConfigureMockMvc(addFilters = false)
public class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DoctorService doctorService;

    private DoctorDTO doctorDTO;

    @BeforeEach
    void setUp() {
        doctorDTO = new DoctorDTO();
        doctorDTO.setId(1L);
        doctorDTO.setFirstName("Иван");
        doctorDTO.setLastName("Петров");
        doctorDTO.setSpecialization("Хирург");
    }

    @Test
    void getAllDoctors_ReturnsList() throws Exception {
        List<DoctorDTO> mockDoctors = List.of(
                new DoctorDTO(1L, "John", "Doe", "Cardiology"),
                new DoctorDTO(2L, "Anna", "Smith", "Neurology")
        );

        when(doctorService.getAllDoctors()).thenReturn(mockDoctors);

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].specialization").value("Neurology"));
    }
    @Test
    void getAllDoctors_EmptyList_ReturnsOk() throws Exception {
        when(doctorService.getAllDoctors()).thenReturn(List.of());

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    void getAllDoctors_ShouldReturnAllDoctors() throws Exception {
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
    void getDoctorById_WhenDoctorExists_ShouldReturnDoctor() throws Exception {
        // Arrange
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
    void getDoctorById_WhenDoctorNotExists_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(doctorService.getDoctorById(1L)).thenThrow(new EntityNotFoundException("Doctor not found with id: 1"));

        // Act & Assert
        mockMvc.perform(get("/api/doctors/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createDoctor_ShouldReturnCreatedDoctor() throws Exception {
        // Arrange
        when(doctorService.createDoctor(any(DoctorDTO.class))).thenReturn(doctorDTO);

        // Act & Assert
        mockMvc.perform(post("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctorDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("Иван"))
                .andExpect(jsonPath("$.lastName").value("Петров"))
                .andExpect(jsonPath("$.specialization").value("Хирург"));
    }

    @Test
    void updateDoctor_WhenDoctorExists_ShouldReturnUpdatedDoctor() throws Exception {
        // Arrange
        when(doctorService.updateDoctor(eq(1L), any(DoctorDTO.class))).thenReturn(doctorDTO);

        // Act & Assert
        mockMvc.perform(put("/api/doctors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctorDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("Иван"))
                .andExpect(jsonPath("$.lastName").value("Петров"))
                .andExpect(jsonPath("$.specialization").value("Хирург"));
    }

    @Test
    void deleteDoctor_ShouldReturnNoContent() throws Exception {
        // Arrange
        // Здесь не нужно мокать, так как метод void

        // Act & Assert
        mockMvc.perform(delete("/api/doctors/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void updateDoctor_WhenDoctorNotExists_ShouldReturnNotFound() throws Exception {
        // Arrange
        DoctorDTO updatedDoctorDTO = new DoctorDTO();
        updatedDoctorDTO.setFirstName("Updated");
        updatedDoctorDTO.setLastName("Name");
        updatedDoctorDTO.setSpecialization("UpdatedSpec");

        when(doctorService.updateDoctor(eq(1L), any(DoctorDTO.class)))
                .thenThrow(new DoctorNotFoundException(1L));

        // Act & Assert
        mockMvc.perform(put("/api/doctors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDoctorDTO)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Doctor not found with id: 1"));
    }

    @Test
    void getDoctorById_WhenDoctorNotFound_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(doctorService.getDoctorById(1L))
                .thenThrow(new EntityNotFoundException("Doctor not found with id: 1"));

        // Act & Assert
        mockMvc.perform(get("/api/doctors/1"))
                .andExpect(status().isNotFound());

        verify(doctorService, times(1)).getDoctorById(1L);
    }
    @Test
    void getDoctorById_ShouldReturnDoctor() throws Exception {
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

    @Test
    void updateDoctor_ShouldReturnUpdatedDoctor() throws Exception {
        // Arrange
        DoctorDTO inputDto = new DoctorDTO(1L, "John", "Doe Updated", "Cardiology");
        DoctorDTO outputDto = new DoctorDTO(1L, "John", "Doe Updated", "Cardiology");

        when(doctorService.updateDoctor(eq(1L), any(DoctorDTO.class))).thenReturn(outputDto);

        // Act & Assert
        mockMvc.perform(put("/api/doctors/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe Updated"))
                .andExpect(jsonPath("$.specialization").value("Cardiology"));

        verify(doctorService, times(1)).updateDoctor(eq(1L), any(DoctorDTO.class));
    }

    @Test
    void getDoctorById_WithInvalidId_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/doctors/abc")) // нечисловой ID
                .andExpect(status().isBadRequest());

        verify(doctorService, never()).getDoctorById(any());
    }

}

