package ru.natali.clinic.exception.medserviceexception;

public class MedserviceSaveException extends MedserviceRepositoryException {
    public MedserviceSaveException(String message) {
        super(message);
    }

    public MedserviceSaveException(String message, Throwable cause) {
        super(message, cause);
    }
}

