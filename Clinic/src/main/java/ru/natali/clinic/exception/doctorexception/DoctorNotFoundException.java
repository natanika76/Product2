package ru.natali.clinic.exception.doctorexception;

import ru.natali.clinic.exception.ClinicException;

public class DoctorNotFoundException extends ClinicException {
    /*public DoctorNotFoundException(Long id, Exception e) {
        super("Врач с таким id " + id + " не найден.");
    }*/
    // Конструктор с сообщением и причиной исключения
    public DoctorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    // Конструктор только с сообщением
    public DoctorNotFoundException(String message) {
        super(message);
    }

    // Конструктор с id и причиной исключения
    public DoctorNotFoundException(Long id, Throwable cause) {
        super("Врач с таким id " + id + " не найден.", cause);
    }

    // Конструктор только с id
    public DoctorNotFoundException(Long id) {
        super("Врач с таким id " + id + " не найден.");
    }
}
