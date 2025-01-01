package ru.natali.clinic.exception.medserviceexception;

public class MedserviceNotFoundException extends MedserviceRepositoryException {
    public MedserviceNotFoundException(String message) {
        super(message);
    }

    public MedserviceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
