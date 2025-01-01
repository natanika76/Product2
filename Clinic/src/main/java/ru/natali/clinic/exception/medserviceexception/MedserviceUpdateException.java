package ru.natali.clinic.exception.medserviceexception;

public class MedserviceUpdateException extends MedserviceRepositoryException {
    public MedserviceUpdateException(String message) {
        super(message);
    }

    public MedserviceUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}
