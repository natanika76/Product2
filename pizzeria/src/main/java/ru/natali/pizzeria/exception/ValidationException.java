package ru.natali.pizzeria.exception;

import java.util.List;

public class ValidationException extends BusinessException {
    public ValidationException(String message) {
        super(ErrorCode.VALIDATION_FAILED, message);
    }

    public ValidationException(List<String> errors) {
        super(ErrorCode.VALIDATION_FAILED, "Validation failed");
        this.errors = errors;
    }

    private List<String> errors;

    public List<String> getErrors() {
        return errors;
    }
}
