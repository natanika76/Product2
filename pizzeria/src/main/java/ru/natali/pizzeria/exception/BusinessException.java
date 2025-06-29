package ru.natali.pizzeria.exception;

public abstract class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public enum ErrorCode {
        NOT_FOUND,
        VALIDATION_FAILED,
        CONFLICT,
        BAD_REQUEST,
        FORBIDDEN
    }
}
