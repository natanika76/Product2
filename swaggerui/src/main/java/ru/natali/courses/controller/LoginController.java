package ru.natali.courses.controller;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.natali.courses.model.Student;

@Controller
@Hidden // Эта аннотация скроет весь контроллер
@Tag(name = "Аутентификация")
public class LoginController {

    @GetMapping("/login")
    @Operation(summary = "Страница входа")
    public String login() {
        return "login";
    }
}