package ru.natali.pizzeria.exception;

public class EntityNotFoundException extends BusinessException {
    public EntityNotFoundException(String entityName, Long id) {
        super(ErrorCode.NOT_FOUND,
                String.format("%s not found with id: %d", entityName, id));
    }
}
