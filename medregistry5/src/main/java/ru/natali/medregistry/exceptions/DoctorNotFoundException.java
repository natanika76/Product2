package ru.natali.medregistry.exceptions;

import jakarta.persistence.EntityNotFoundException;
/**
 * Исключение, выбрасываемое когда врач не найден.
 */
public class DoctorNotFoundException extends EntityNotFoundException {
    public DoctorNotFoundException(Long id) {
        super("Doctor not found with id: " + id);
    }
}
