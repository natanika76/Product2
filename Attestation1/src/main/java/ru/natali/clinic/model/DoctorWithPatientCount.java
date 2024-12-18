package ru.natali.clinic.model;

public record DoctorWithPatientCount(long id, String firstName, String lastName, int patientCount) {}