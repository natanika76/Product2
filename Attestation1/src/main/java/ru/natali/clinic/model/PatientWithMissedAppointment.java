package ru.natali.clinic.model;

import java.time.LocalDate;

public record PatientWithMissedAppointment(long id, String firstName, String lastName, LocalDate lastAppointmentDate) {}
