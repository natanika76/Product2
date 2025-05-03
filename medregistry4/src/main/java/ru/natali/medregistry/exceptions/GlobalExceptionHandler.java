package ru.natali.medregistry.exceptions;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import org.springframework.web.context.request.WebRequest;
/**
 * Глобальный обработчик исключений приложения.
 * Перехватывает исключения и возвращает стандартизированные ответы.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Обрабатывает исключения, связанные с ненайденными сущностями.
     *
     * @param ex исключение
     * @param request web-запрос
     * @return ResponseEntity с информацией об ошибке и HTTP статусом 404
     */
    @ExceptionHandler({DoctorNotFoundException.class, EntityNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFoundException(RuntimeException ex, WebRequest request) {
        ErrorResponse error = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found",
                ex.getMessage(),
                request.getDescription(false).replace("uri=", "") // получаем путь из запроса
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Класс для представления информации об ошибке.
     */
    @Data
    @AllArgsConstructor
    public static class ErrorResponse {
        /** Временная метка возникновения ошибки */
        private LocalDateTime timestamp;

        /** HTTP статус код */
        private int status;

        /** Название ошибки */
        private String error;

        /** Сообщение об ошибке */
        private String message;

        /** Путь запроса, вызвавшего ошибку */
        private String path;
    }
}