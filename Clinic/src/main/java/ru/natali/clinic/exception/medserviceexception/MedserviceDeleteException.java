package ru.natali.clinic.exception.medserviceexception;

public class MedserviceDeleteException extends MedserviceRepositoryException {
    public MedserviceDeleteException(String message) {
        super(message);
    }

    public MedserviceDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
