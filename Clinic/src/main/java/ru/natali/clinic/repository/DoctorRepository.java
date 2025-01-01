package ru.natali.clinic.repository;

import ru.natali.clinic.model.Doctor;
import ru.natali.clinic.model.DoctorWithAppointmentCount;
import ru.natali.clinic.model.DoctorWithPatientCount;
import ru.natali.clinic.model.Patient;

import java.util.List;
import java.util.Map;

public interface DoctorRepository extends CrudRepository<Doctor> {
    List<Doctor> findAllByFirstName(String firstName);
    List<Doctor> findDoctorsSortedByLastName();
    Map<Long, Integer> countAppointmentsPerDoctor();
    List<DoctorWithPatientCount> getDoctorsWithCountsPatientsByMonth();
    List<DoctorWithAppointmentCount> getBusyDoctors();
    void update(Long id, Doctor updatedDoctor);

    Doctor findById(Long id);
}
