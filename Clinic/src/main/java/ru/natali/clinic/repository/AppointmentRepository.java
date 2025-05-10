package ru.natali.clinic.repository;

import ru.natali.clinic.model.Appointment;
import java.util.List;

public interface AppointmentRepository extends CrudRepository<Appointment> {
    List<Appointment> findByStatus(String status);
}
