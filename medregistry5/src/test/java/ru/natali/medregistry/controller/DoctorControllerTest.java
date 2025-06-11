package ru.natali.medregistry.controller;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import ru.natali.medregistry.dto.DoctorDTO;
import ru.natali.medregistry.service.DoctorService;
import ru.natali.medregistry.service.kafka.DoctorEventProducer;


import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorControllerTest {

    @Mock
    private DoctorService doctorService;

    @Mock
    private DoctorEventProducer doctorEventProducer;

    @InjectMocks
    private DoctorController doctorController;

    private DoctorDTO doctorDTO;
    private DoctorDTO updatedDoctorDTO;

    @BeforeEach
    void setUp() {
        doctorDTO = new DoctorDTO(1L, "John", "Doe", "Cardiology");
        updatedDoctorDTO = new DoctorDTO(1L, "John", "Doe Updated", "Cardiology");
    }

    @Test
    void getAllDoctors_shouldReturnListOfDoctors() {
        // Arrange
        List<DoctorDTO> expectedDoctors = Arrays.asList(doctorDTO);
        when(doctorService.getAllDoctors()).thenReturn(expectedDoctors);

        // Act
        ResponseEntity<List<DoctorDTO>> response = doctorController.getAllDoctors();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDoctors, response.getBody());
        verify(doctorService).getAllDoctors();
    }

    @Test
    void getDoctorById_shouldReturnDoctor() {
        // Arrange
        when(doctorService.getDoctorById(1L)).thenReturn(doctorDTO);

        // Act
        ResponseEntity<DoctorDTO> response = doctorController.getDoctorById(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(doctorDTO, response.getBody());
        verify(doctorService).getDoctorById(1L);
    }

    @Test
    void getDoctorById_notFound_shouldThrowException() {
        // Arrange
        when(doctorService.getDoctorById(1L)).thenThrow(new EntityNotFoundException("Doctor not found"));

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> doctorController.getDoctorById(1L));
    }

    @Test
    void createDoctor_shouldReturnCreatedDoctor() {
        // Arrange
        when(doctorService.createDoctor(doctorDTO)).thenReturn(doctorDTO);

        // Act
        ResponseEntity<DoctorDTO> response = doctorController.createDoctor(doctorDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(doctorDTO, response.getBody());
        verify(doctorService).createDoctor(doctorDTO);
        verify(doctorEventProducer).sendDoctorCreatedEvent(doctorDTO);
    }

    @Test
    void updateDoctor_shouldReturnUpdatedDoctor() {
        // Arrange
        when(doctorService.updateDoctor(1L, updatedDoctorDTO)).thenReturn(updatedDoctorDTO);

        // Act
        ResponseEntity<DoctorDTO> response = doctorController.updateDoctor(1L, updatedDoctorDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedDoctorDTO, response.getBody());
        verify(doctorService).updateDoctor(1L, updatedDoctorDTO);
        verify(doctorEventProducer).sendDoctorUpdatedEvent(updatedDoctorDTO);
    }

    @Test
    void deleteDoctor_shouldReturnNoContent() {
        // Arrange
        doNothing().when(doctorService).deleteDoctor(1L);

        // Act
        ResponseEntity<Void> response = doctorController.deleteDoctor(1L);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(doctorService).deleteDoctor(1L);
        verify(doctorEventProducer).sendDoctorDeletedEvent(1L);
    }
}