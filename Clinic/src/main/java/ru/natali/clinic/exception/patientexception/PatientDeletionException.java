package ru.natali.clinic.exception.patientexception;

import ru.natali.clinic.exception.ClinicException;

public class PatientDeletionException extends ClinicException {
    public PatientDeletionException(String message) {
        super(message);
    }

    public PatientDeletionException(String message, Throwable cause) {
        super(message, cause);
    }
}
