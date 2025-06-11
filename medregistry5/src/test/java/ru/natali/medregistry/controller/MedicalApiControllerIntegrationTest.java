package ru.natali.medregistry.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.natali.medregistry.dto.PatientDTO;
import ru.natali.medregistry.service.PatientService;
import ru.natali.medregistry.service.kafka.PatientEventProducer;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MedicalApiControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PatientService patientService;

    @MockBean
    private PatientEventProducer patientEventProducer;

    private PatientDTO patientDTO;

    @BeforeEach
    void setUp() {
        patientDTO = new PatientDTO();
        patientDTO.setId(1L);
        patientDTO.setFirstName("Иван");
        patientDTO.setLastName("Иванов");
        patientDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patientDTO.setInsuranceNumber("1234567890");
    }

    @Test
    void createPatient_shouldReturn201() throws Exception {
        given(patientService.createPatient(any(PatientDTO.class))).willReturn(patientDTO);
        doNothing().when(patientEventProducer).sendPatientCreatedEvent(any(PatientDTO.class));

        mockMvc.perform(post("/api/medical/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Иван"))
                .andExpect(jsonPath("$.lastName").value("Иванов"))
                .andExpect(jsonPath("$.dateOfBirth").value("1990-01-01"));
    }

    @Test
    void getPatientByInsuranceNumber_shouldReturn200() throws Exception {
        given(patientService.getPatientByInsuranceNumber("1234567890")).willReturn(patientDTO);

        mockMvc.perform(get("/api/medical/patients/insurance/1234567890"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.insuranceNumber").value("1234567890"))
                .andExpect(jsonPath("$.dateOfBirth").value("1990-01-01"));
    }

    @Test
    void getPatientById_shouldReturn200() throws Exception {
        given(patientService.getPatientById(1L)).willReturn(patientDTO);

        mockMvc.perform(get("/api/medical/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("Иван"));
    }

    @Test
    void updatePatient_shouldReturn200() throws Exception {
        given(patientService.updatePatient(eq(1L), any(PatientDTO.class))).willReturn(patientDTO);
        doNothing().when(patientEventProducer).sendPatientUpdatedEvent(any(PatientDTO.class));

        mockMvc.perform(put("/api/medical/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Иванов"));
    }

    @Test
    void deletePatient_shouldReturn204() throws Exception {
        doNothing().when(patientService).deletePatient(1L);
        doNothing().when(patientEventProducer).sendPatientDeletedEvent(1L);

        mockMvc.perform(delete("/api/medical/patients/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void createPatient_withInvalidDate_shouldReturn400() throws Exception {
        String invalidJson = """
    {
        "firstName": "Иван",
        "lastName": "Иванов",
        "dateOfBirth": "1990/01/01",  // Неправильный формат
        "insuranceNumber": "1234567890"
    }
    """;

        mockMvc.perform(post("/api/medical/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
}
