package ru.natali.medregistry.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import jakarta.persistence.EntityNotFoundException;
import ru.natali.medregistry.dto.PatientDTO;
import ru.natali.medregistry.service.PatientService;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicalApiController.class)
class MedicalApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    private PatientDTO testPatientDTO;

    @BeforeEach
    void setUp() {
        testPatientDTO = new PatientDTO();
        testPatientDTO.setId(1L);
        testPatientDTO.setFirstName("Иван");
        testPatientDTO.setLastName("Иванов");
        testPatientDTO.setDateOfBirth(LocalDate.of(1980, 1, 1));
        testPatientDTO.setInsuranceNumber("1234567890");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createPatient_ShouldReturnCreatedStatus() throws Exception {
        when(patientService.createPatient(any(PatientDTO.class))).thenReturn(testPatientDTO);

        mockMvc.perform(post("/api/medical/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Иван\",\"lastName\":\"Иванов\",\"dateOfBirth\":\"1980-01-01\",\"insuranceNumber\":\"1234567890\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser
    void getPatientById_ShouldReturnPatient() throws Exception {
        when(patientService.getPatientById(1L)).thenReturn(testPatientDTO);

        mockMvc.perform(get("/api/medical/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Иван"));
    }

    @Test
    @WithMockUser
    void getPatientByInsuranceNumber_ShouldReturnPatient() throws Exception {
        when(patientService.getPatientByInsuranceNumber("1234567890")).thenReturn(testPatientDTO);

        mockMvc.perform(get("/api/medical/patients/insurance/1234567890"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.insuranceNumber").value("1234567890"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void updatePatient_ShouldReturnUpdatedPatient() throws Exception {
        when(patientService.updatePatient(eq(1L), any(PatientDTO.class))).thenReturn(testPatientDTO);

        mockMvc.perform(put("/api/medical/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\":\"Иван\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void deletePatient_ShouldReturnNoContent() throws Exception {
        doNothing().when(patientService).deletePatient(1L);

        mockMvc.perform(delete("/api/medical/patients/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void getPatientById_ShouldReturnNotFound() throws Exception {
        when(patientService.getPatientById(999L))
                .thenThrow(new EntityNotFoundException("Patient not found"));

        mockMvc.perform(get("/api/medical/patients/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Patient not found"))
                .andExpect(jsonPath("$.path").value("/api/medical/patients/999"));
    }
    @Test
    @WithMockUser
    void getPatientByInsuranceNumber_ShouldReturnNotFound() throws Exception {
        // Подготовка
        String invalidInsuranceNumber = "0000000000";
        String errorMessage = "Patient not found with insurance number: " + invalidInsuranceNumber;

        when(patientService.getPatientByInsuranceNumber(invalidInsuranceNumber))
                .thenThrow(new EntityNotFoundException(errorMessage));

        // Выполнение и проверка
        mockMvc.perform(get("/api/medical/patients/insurance/{number}", invalidInsuranceNumber))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.timestamp").exists())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value(errorMessage))
                .andExpect(jsonPath("$.path").value("/api/medical/patients/insurance/" + invalidInsuranceNumber));
    }
}