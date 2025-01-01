package ru.natali.clinic.exception.doctorexception;

import ru.natali.clinic.exception.ClinicException;

public class DoctorDeleteException extends ClinicException {

    // Конструктор для случая, когда передается ID врача
    public DoctorDeleteException(Long id) {
        super("Не удалось удалить врача с id " + id);
    }

    // Конструктор для случая, когда передается ID врача и причина исключения
    public DoctorDeleteException(Long id, Throwable cause) {
        super("Не удалось удалить врача с id " + id, cause);
    }

    // Конструктор для случая, когда передается только сообщение об ошибке
    public DoctorDeleteException(String message) {
        super(message);
    }

    // Конструктор для случая, когда передается сообщение об ошибке и причина исключения
    public DoctorDeleteException(String message, Throwable cause) {
        super(message, cause);
    }
}
