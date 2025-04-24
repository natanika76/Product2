package ru.natali.courses.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.natali.courses.model.Student;

@Controller
public class LoginController {

    // GET-обработчик для отображения страницы входа
    @GetMapping("/login")
    public String login() {
        return "login";  // login.html в папке templates/
    }
}