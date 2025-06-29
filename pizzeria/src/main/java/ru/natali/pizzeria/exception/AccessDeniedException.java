package ru.natali.pizzeria.exception;

public class AccessDeniedException extends BusinessException {
    public AccessDeniedException(String message) {
        super(ErrorCode.FORBIDDEN, message);
    }
}