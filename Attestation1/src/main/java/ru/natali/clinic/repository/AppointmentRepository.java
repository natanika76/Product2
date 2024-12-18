package ru.natali.clinic.repository;

import ru.natali.clinic.model.Appointment;
import ru.natali.clinic.model.AppointmentDetails;
import ru.natali.clinic.model.PatientWithMissedAppointment;

import java.util.List;

public interface AppointmentRepository extends CrudRepository<Appointment> {
    List<Appointment> findByStatus(String status);
    List<AppointmentDetails> getAppointmentDetails();
    List<Appointment> findRecentMedicalRecordsAppointments();
    List<PatientWithMissedAppointment> getPatientsWithMissedAppointments();
}
