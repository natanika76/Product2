package ru.natali.clinic.repository;

import ru.natali.clinic.model.Doctor;
import ru.natali.clinic.model.Patient;

import java.util.List;

public interface DoctorRepository extends CrudRepository<Doctor> {
    List<Doctor> findAllByFirstName(String firstName);
}
