package ru.natali.courses.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.natali.courses.service.StudentService;

@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private StudentService studentService;

    //GET http://localhost:8080/api/test/check-password
    @GetMapping("/check-password")
    public String checkPassword() {
        boolean result = studentService.checkPassword("123", "{noop}123");
        return "Проверка пароля: " + (result ? "УСПЕШНО" : "ОШИБКА");
    }
}
