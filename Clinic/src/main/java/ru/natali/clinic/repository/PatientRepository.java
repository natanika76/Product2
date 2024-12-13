package ru.natali.clinic.repository;

import ru.natali.clinic.model.Patient;
import java.util.List;

public interface PatientRepository extends CrudRepository<Patient>{
    List<Patient> findAllByFirstName(String firstName);
    Patient updatePatient(Long id, String newFirstName, String newLastName);
}
