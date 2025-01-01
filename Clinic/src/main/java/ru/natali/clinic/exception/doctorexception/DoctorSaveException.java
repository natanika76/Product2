package ru.natali.clinic.exception.doctorexception;

import ru.natali.clinic.exception.ClinicException;

public class DoctorSaveException extends ClinicException {
    public DoctorSaveException(String message) {
        super("Не удалось сохранение: " + message);
    }

    public DoctorSaveException(String message, Throwable cause) {
        super("Не удалось сохранение: " + message, cause);
    }
}
