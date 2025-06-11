package ru.natali.medregistry.controller;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.natali.medregistry.dto.PatientDTO;
import ru.natali.medregistry.model.Patient;
import ru.natali.medregistry.service.PatientService;
import ru.natali.medregistry.service.kafka.PatientEventProducer;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalApiControllerTest {

    @Mock
    private PatientService patientService;

    @Mock
    private PatientEventProducer patientEventProducer;

    @InjectMocks
    private MedicalApiController medicalApiController;

    private PatientDTO patientDTO;
    private PatientDTO updatedPatientDTO;

    @BeforeEach
    void setUp() {
        patientDTO = new PatientDTO();
        patientDTO.setId(1L);
        patientDTO.setFirstName("Иван");
        patientDTO.setLastName("Иванов");
        patientDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));
        patientDTO.setInsuranceNumber("1234567890");

        updatedPatientDTO = new PatientDTO();
        updatedPatientDTO.setId(1L);
        updatedPatientDTO.setFirstName("Петр");
        updatedPatientDTO.setLastName("Иванов");
        updatedPatientDTO.setDateOfBirth(LocalDate.of(1990, 1, 1));
        updatedPatientDTO.setInsuranceNumber("1234567890");
    }

    @Test
    void createPatient_shouldReturnCreatedPatient() {
        when(patientService.createPatient(patientDTO)).thenReturn(patientDTO);

        ResponseEntity<PatientDTO> response = medicalApiController.createPatient(patientDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(patientDTO, response.getBody());
        assertEquals("Иван", response.getBody().getFirstName());
        assertEquals("Иванов", response.getBody().getLastName());
        verify(patientService).createPatient(patientDTO);
        verify(patientEventProducer).sendPatientCreatedEvent(patientDTO);
    }

    @Test
    void getPatientByInsuranceNumber_shouldReturnPatient() {
        when(patientService.getPatientByInsuranceNumber("1234567890")).thenReturn(patientDTO);

        ResponseEntity<PatientDTO> response = medicalApiController.getPatientByInsuranceNumber("1234567890");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(patientDTO, response.getBody());
        assertEquals(LocalDate.of(1990, 1, 1), response.getBody().getDateOfBirth());
        verify(patientService).getPatientByInsuranceNumber("1234567890");
    }

    @Test
    void getPatientById_shouldReturnPatient() {
        when(patientService.getPatientById(1L)).thenReturn(patientDTO);

        ResponseEntity<PatientDTO> response = medicalApiController.getPatientById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(patientDTO, response.getBody());
        assertEquals("1234567890", response.getBody().getInsuranceNumber());
        verify(patientService).getPatientById(1L);
    }

    @Test
    void getAllPatients_shouldReturnAllPatients() {
        Patient patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("Иван");

        when(patientService.getAllPatients()).thenReturn(Arrays.asList(patient));

        List<Patient> patients = medicalApiController.getAllPatients();

        assertEquals(1, patients.size());
        assertEquals("Иван", patients.get(0).getFirstName());
        verify(patientService).getAllPatients();
    }

    @Test
    void updatePatient_shouldReturnUpdatedPatient() {
        when(patientService.updatePatient(1L, updatedPatientDTO)).thenReturn(updatedPatientDTO);

        ResponseEntity<PatientDTO> response = medicalApiController.updatePatient(1L, updatedPatientDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Петр", response.getBody().getFirstName());
        verify(patientService).updatePatient(1L, updatedPatientDTO);
        verify(patientEventProducer).sendPatientUpdatedEvent(updatedPatientDTO);
    }

    @Test
    void deletePatient_shouldReturnNoContent() {
        doNothing().when(patientService).deletePatient(1L);

        ResponseEntity<Void> response = medicalApiController.deletePatient(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(patientService).deletePatient(1L);
        verify(patientEventProducer).sendPatientDeletedEvent(1L);
    }
}