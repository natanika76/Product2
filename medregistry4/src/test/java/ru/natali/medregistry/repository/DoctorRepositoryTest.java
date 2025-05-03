package ru.natali.medregistry.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import ru.natali.medregistry.model.Doctor;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@TestPropertySource(locations = "classpath:application.properties")
class DoctorRepositoryTest {
    @Autowired
    private DoctorRepository doctorRepository;

    @Test
    public void testSaveDoctor() {
        Doctor doctor = new Doctor();
        doctor.setFirstName("John");
        doctor.setLastName("Doe");
        doctor.setSpecialization("Cardiology");

        Doctor savedDoctor = doctorRepository.save(doctor);

        assertNotNull(savedDoctor.getId());
        assertEquals("John", savedDoctor.getFirstName());
        assertEquals("Doe", savedDoctor.getLastName());
        assertEquals("Cardiology", savedDoctor.getSpecialization());
        assertFalse(savedDoctor.isDeleted());
    }
    @Test
    public void testFindAllActive() {
        // Create and save active doctors
        Doctor doctor1 = new Doctor();
        doctor1.setFirstName("Alice");
        doctor1.setLastName("Smith");
        doctor1.setSpecialization("Neurology");
        doctorRepository.save(doctor1);

        Doctor doctor2 = new Doctor();
        doctor2.setFirstName("Bob");
        doctor2.setLastName("Johnson");
        doctor2.setSpecialization("Pediatrics");
        doctorRepository.save(doctor2);

        // Create and save deleted doctor
        Doctor doctor3 = new Doctor();
        doctor3.setFirstName("Deleted");
        doctor3.setLastName("Doctor");
        doctor3.setSpecialization("Oncology");
        doctor3.setDeleted(true);
        doctorRepository.save(doctor3);

        List<Doctor> activeDoctors = doctorRepository.findAllActive();

        assertEquals(2, activeDoctors.size());
        assertTrue(activeDoctors.stream().anyMatch(d -> d.getFirstName().equals("Alice")));
        assertTrue(activeDoctors.stream().anyMatch(d -> d.getFirstName().equals("Bob")));
        assertFalse(activeDoctors.stream().anyMatch(d -> d.getFirstName().equals("Deleted")));
    }
    @Test
    public void testFindById() {
        Doctor doctor = new Doctor();
        doctor.setFirstName("Sarah");
        doctor.setLastName("Connor");
        doctor.setSpecialization("Gynecology");
        Doctor savedDoctor = doctorRepository.save(doctor);

        Optional<Doctor> foundDoctor = doctorRepository.findById(savedDoctor.getId());

        assertTrue(foundDoctor.isPresent());
        assertEquals("Sarah", foundDoctor.get().getFirstName());
        assertEquals("Connor", foundDoctor.get().getLastName());
        assertEquals("Gynecology", foundDoctor.get().getSpecialization());
    }
}