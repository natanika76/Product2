package ru.natali.clinic.exception.medserviceexception;

import ru.natali.clinic.exception.ClinicException;

public class MedserviceRepositoryException extends ClinicException {
    public MedserviceRepositoryException(String message) {
        super(message);
    }

    public MedserviceRepositoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
