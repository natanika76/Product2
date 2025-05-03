package ru.natali.medregistry.service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import ru.natali.medregistry.dto.PatientDTO;
import ru.natali.medregistry.model.Patient;
import ru.natali.medregistry.repository.PatientRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class PatientServiceImplTest {

    @MockBean
    private PatientRepository patientRepository;

    @Autowired
    private PatientService patientService;

    @Autowired
    private ModelMapper modelMapper;

    private PatientDTO testPatientDTO;
    private Patient testPatient;

    @BeforeEach
    void setUp() {
        testPatientDTO = new PatientDTO(
                1L,
                "John",
                "Doe",
                LocalDate.of(1980, 1, 1),
                "INS123456"
        );

        testPatient = new Patient();
        testPatient.setId(1L);
        testPatient.setFirstName("John");
        testPatient.setLastName("Doe");
        testPatient.setDateOfBirth(LocalDate.of(1980, 1, 1));
        testPatient.setInsuranceNumber("INS123456");
        testPatient.setDeleted(false);
    }

    @Test
    void createPatient_ShouldReturnSavedPatient_WhenInsuranceNumberIsUnique() {
        when(patientRepository.existsByInsuranceNumber("INS123456")).thenReturn(false);
        when(patientRepository.save(any(Patient.class))).thenReturn(testPatient);

        PatientDTO result = patientService.createPatient(testPatientDTO);

        assertThat(result, notNullValue());
        assertThat(result.getId(), equalTo(1L));
        assertThat(result.getFirstName(), equalTo("John"));
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void createPatient_ShouldThrowException_WhenInsuranceNumberExists() {
        when(patientRepository.existsByInsuranceNumber("INS123456")).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            patientService.createPatient(testPatientDTO);
        });

        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void updatePatient_ShouldUpdatePatient_WhenInsuranceNumberIsUniqueOrUnchanged() {
        Patient updatedPatient = new Patient();
        updatedPatient.setId(1L);
        updatedPatient.setFirstName("Updated");
        updatedPatient.setLastName("Name");
        updatedPatient.setDateOfBirth(LocalDate.of(1985, 1, 1));
        updatedPatient.setInsuranceNumber("INS123456");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(patientRepository.existsByInsuranceNumber("INS123456")).thenReturn(false);
        when(patientRepository.save(any(Patient.class))).thenReturn(updatedPatient);

        PatientDTO updatedDTO = new PatientDTO();
        updatedDTO.setFirstName("Updated");
        updatedDTO.setLastName("Name");
        updatedDTO.setDateOfBirth(LocalDate.of(1985, 1, 1));
        updatedDTO.setInsuranceNumber("INS123456");

        PatientDTO result = patientService.updatePatient(1L, updatedDTO);

        assertThat(result.getFirstName(), equalTo("Updated"));
        assertThat(result.getLastName(), equalTo("Name"));
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void updatePatient_ShouldThrowException_WhenInsuranceNumberExistsAndChanged() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        when(patientRepository.existsByInsuranceNumber("NEWINS123")).thenReturn(true);

        PatientDTO updatedDTO = new PatientDTO();
        updatedDTO.setInsuranceNumber("NEWINS123");

        assertThrows(IllegalArgumentException.class, () -> {
            patientService.updatePatient(1L, updatedDTO);
        });

        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void getPatientById_ShouldReturnPatient_WhenExists() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));

        PatientDTO result = patientService.getPatientById(1L);

        assertThat(result, notNullValue());
        assertThat(result.getId(), equalTo(1L));
    }

    @Test
    void getPatientById_ShouldThrowException_WhenNotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            patientService.getPatientById(1L);
        });
    }

    @Test
    void deletePatient_ShouldSoftDeletePatient_WhenExists() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(testPatient));
        doNothing().when(patientRepository).softDelete(1L);

        patientService.deletePatient(1L);

        verify(patientRepository, times(1)).softDelete(1L);
    }

    @Test
    void getAllPatients_ShouldReturnAllActivePatients() {
        // Убедитесь, что тестовый пациент не помечен как удаленный
        testPatient.setDeleted(false);

        List<Patient> patients = List.of(testPatient);

        // Мокируем правильный метод - findAll() вместо findAllActive()
        when(patientRepository.findAll()).thenReturn(patients);

        List<Patient> result = patientService.getAllPatients();

        assertThat(result, hasSize(1));
        assertThat(result.get(0).getFirstName(), equalTo("John"));
    }

    @Test
    void convertToDto_ShouldConvertCorrectly() {
        PatientDTO result = patientService.convertToDto(testPatient);

        assertThat(result.getId(), equalTo(1L));
        assertThat(result.getFirstName(), equalTo("John"));
        assertThat(result.getLastName(), equalTo("Doe"));
    }

    @Test
    void convertToEntity_ShouldConvertCorrectly() {
        Patient result = patientService.convertToEntity(testPatientDTO);

        assertThat(result.getId(), equalTo(1L));
        assertThat(result.getFirstName(), equalTo("John"));
        assertThat(result.getLastName(), equalTo("Doe"));
    }
}
