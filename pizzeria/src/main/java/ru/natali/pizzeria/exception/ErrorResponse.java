package ru.natali.pizzeria.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private String code;
    private String message;
    private Instant timestamp;
    private List<String> details;

    public ErrorResponse(BusinessException ex) {
        this(ex.getErrorCode().name(), ex.getMessage(), Instant.now(), null);
    }

    public ErrorResponse(String code, String message) {
        this(code, message, Instant.now(), null);
    }

    public ErrorResponse(String code, String message, List<String> details) {
        this(code, message, Instant.now(), details);
    }
}