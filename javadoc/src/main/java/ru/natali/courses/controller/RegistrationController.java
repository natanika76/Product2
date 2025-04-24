package ru.natali.courses.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.natali.courses.model.Role;
import ru.natali.courses.model.Student;
import ru.natali.courses.repository.StudentRepository;

@Controller
@RequestMapping("/register")
public class RegistrationController {

    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

     // Отображение страницы регистрации.
    @GetMapping
    public String showRegistrationForm(Model model) {
        model.addAttribute("student", new Student());
        return "registration"; // Имя шаблона Thymeleaf
    }

    @GetMapping("/registration-success")
    public String registrationSuccess() {
        return "registration-success"; // Открывает registration-success.html
    }

    // Обработка формы регистрации и сохранение нового студента
    @PostMapping
    public String registerNewStudent(@Valid Student student, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "registration";
        }

        // Хэширование пароля
        String encodedPassword = passwordEncoder.encode(student.getPassword());
        student.setPassword(encodedPassword);

        // Установка роли
        student.setRole(Role.USER);

        // Сохранение пользователя
        studentRepository.save(student);

        // Подтверждение успеха
        model.addAttribute("message", "Вы успешно зарегистрированы! Теперь можете войти.");
        return "registration-success";
    }

}
