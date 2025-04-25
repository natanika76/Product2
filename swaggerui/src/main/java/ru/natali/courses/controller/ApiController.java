package ru.natali.courses.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "API Тестирование", description = "Операции для тестирования работы API")
public class ApiController {

    @GetMapping("/test")
    @Operation(summary = "Проверка работы API", description = "Возвращает тестовое сообщение")
    @ApiResponse(responseCode = "200", description = "Успешный ответ")
    public ResponseEntity<String> testApi() {
        return ResponseEntity.ok("{\"message\": \"API works!\"}");
    }
}
