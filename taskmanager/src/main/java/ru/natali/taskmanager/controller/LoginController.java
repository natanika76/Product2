package ru.natali.taskmanager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    //http://localhost:8080/login
    @GetMapping("/login")  // Теперь обрабатывает именно "/login"
    public String showLoginPage() {
        return "login";  // Ищет templates/login.html
    }
}
