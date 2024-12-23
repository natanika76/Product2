package ru.natali.clinic.repository;

import ru.natali.clinic.model.Patient;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PatientRepository extends CrudRepository<Patient>{
    List<Patient> findAllByFirstName(String firstName);
    Patient updatePatient(Long id, String newFirstName, String newLastName);
    Optional<List<Patient>> findPatientsByDoctorId(long doctorId);
    List<Patient> findPatientsByFirstNameStartingWith(String letter);
    List<Patient> findTopNPatients(int n);
    List<Patient> findPatientsByBirthYearAndAddress(LocalDate birthYear, String address);
    List<Patient> findPatientsWithoutMedicalRecords();
    List<Patient> findPatientsBornToday();
}
