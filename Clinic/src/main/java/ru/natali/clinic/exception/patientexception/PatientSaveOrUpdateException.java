package ru.natali.clinic.exception.patientexception;

import ru.natali.clinic.exception.ClinicException;

public class PatientSaveOrUpdateException extends ClinicException {
    public PatientSaveOrUpdateException(String message) {
        super(message);
    }

    public PatientSaveOrUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
