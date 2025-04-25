package ru.natali.courses.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.natali.courses.service.StudentService;

@RestController
@RequestMapping("/api/test")
@Tag(name = "Тестовые операции", description = "Методы для тестирования функциональности")
public class TestController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/check-password")
    @Operation(summary = "Проверка пароля", description = "Тестовый метод для проверки работы шифрования паролей")
    @ApiResponse(responseCode = "200", description = "Результат проверки")
    public String checkPassword() {
        boolean result = studentService.checkPassword("123", "{noop}123");
        return "Проверка пароля: " + (result ? "УСПЕШНО" : "ОШИБКА");
    }
}
