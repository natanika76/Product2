package ru.natali.clinic.exception.patientexception;

import ru.natali.clinic.exception.ClinicException;

public class PatientNotFoundException extends ClinicException {
    public PatientNotFoundException(String message) {
        super(message);
    }

    public PatientNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
