package ru.natali.clinic.exception.doctorexception;

import ru.natali.clinic.exception.ClinicException;

public class DoctorUpdateException extends ClinicException {

    // Конструктор для случая, когда передается ID врача
    public DoctorUpdateException(Long id) {
        super("Не удалось обновление данных врача с id " + id);
    }

    // Конструктор для случая, когда передается ID врача и причина исключения
    public DoctorUpdateException(Long id, Throwable cause) {
        super("Не удалось обновление данных врача с id " + id, cause);
    }

    // Конструктор для случая, когда передается только сообщение об ошибке
    public DoctorUpdateException(String message) {
        super(message);
    }

    // Конструктор для случая, когда передается сообщение об ошибке и причина исключения
    public DoctorUpdateException(String message, Throwable cause) {
        super(message, cause);
    }
}