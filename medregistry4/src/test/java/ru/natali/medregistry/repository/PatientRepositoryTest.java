package ru.natali.medregistry.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import ru.natali.medregistry.model.Patient;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PatientRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PatientRepository patientRepository;

    @Test
    void existsByInsuranceNumber_WhenExists_ReturnsTrue() {
        // Arrange
        Patient patient = new Patient();
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setDateOfBirth(LocalDate.of(1980, 1, 1));
        patient.setInsuranceNumber("INS123");
        entityManager.persist(patient);

        // Act
        boolean exists = patientRepository.existsByInsuranceNumber("INS123");

        // Assert
        assertTrue(exists);
    }
    @Test
    void existsByInsuranceNumber_WhenNotExists_ReturnsFalse() {
        // Act
        boolean exists = patientRepository.existsByInsuranceNumber("NON_EXISTENT");

        // Assert
        assertFalse(exists);
    }
    @Test
    void findByInsuranceNumber_WhenExists_ReturnsPatient() {
        // Arrange
        Patient patient = new Patient();
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setDateOfBirth(LocalDate.of(1980, 1, 1));
        patient.setInsuranceNumber("INS123");
        entityManager.persist(patient);

        // Act
        Optional<Patient> result = patientRepository.findByInsuranceNumber("INS123");

        // Assert
        assertTrue(result.isPresent());
        assertEquals("INS123", result.get().getInsuranceNumber());
    }
    @Test
    void findByInsuranceNumber_WhenNotExists_ReturnsEmpty() {
        // Act
        Optional<Patient> result = patientRepository.findByInsuranceNumber("NON_EXISTENT");

        // Assert
        assertFalse(result.isPresent());
    }
    @Test
    void findAllActive_ReturnsOnlyNonDeletedPatients() {
        // Arrange
        Patient activePatient = new Patient();
        activePatient.setFirstName("John");
        activePatient.setLastName("Doe");
        activePatient.setDateOfBirth(LocalDate.of(1980, 1, 1));
        activePatient.setInsuranceNumber("INS123");
        entityManager.persist(activePatient);

        Patient deletedPatient = new Patient();
        deletedPatient.setFirstName("Jane");
        deletedPatient.setLastName("Doe");
        deletedPatient.setDateOfBirth(LocalDate.of(1985, 1, 1));
        deletedPatient.setInsuranceNumber("INS456");
        deletedPatient.setDeleted(true);
        entityManager.persist(deletedPatient);

        // Act
        List<Patient> result = patientRepository.findAllActive();

        // Assert
        assertEquals(1, result.size());
        assertEquals("INS123", result.get(0).getInsuranceNumber());
    }

    @Test
    void save_WithUniqueInsuranceNumber_Success() {
        // Arrange
        Patient newPatient = new Patient();
        newPatient.setFirstName("John");
        newPatient.setLastName("Doe");
        newPatient.setDateOfBirth(LocalDate.of(1980, 1, 1));
        newPatient.setInsuranceNumber("UNIQUE123");

        // Act
        Patient savedPatient = patientRepository.save(newPatient);

        // Assert
        assertNotNull(savedPatient.getId());
        assertEquals("UNIQUE123", savedPatient.getInsuranceNumber());
    }
    @Test
    void save_WithDuplicateInsuranceNumber_ThrowsException() {
        // Arrange
        Patient existingPatient = new Patient();
        existingPatient.setFirstName("John");
        existingPatient.setLastName("Doe");
        existingPatient.setDateOfBirth(LocalDate.of(1980, 1, 1));
        existingPatient.setInsuranceNumber("DUPLICATE123");
        entityManager.persist(existingPatient);

        Patient newPatient = new Patient();
        newPatient.setFirstName("Jane");
        newPatient.setLastName("Doe");
        newPatient.setDateOfBirth(LocalDate.of(1985, 1, 1));
        newPatient.setInsuranceNumber("DUPLICATE123");

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> {
            patientRepository.save(newPatient);
        });
    }
}