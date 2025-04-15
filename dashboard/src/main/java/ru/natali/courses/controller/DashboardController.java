package ru.natali.courses.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.natali.courses.dto.SimpleCourseDTO;
import ru.natali.courses.model.Student;
import ru.natali.courses.service.CourseService;
import ru.natali.courses.service.StudentService;

import java.util.List;

@Controller
public class DashboardController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Получаем имя текущего пользователя
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Найдем студента по имени пользователя
        Student currentUser = studentService.findByUsername(username);

        // Получаем список курсов, на которые подписан текущий студент
        List<SimpleCourseDTO> courses = courseService.getCoursesForStudent(currentUser.getId());

        // Добавляем данные в модель
        model.addAttribute("username", username);
        model.addAttribute("courses", courses);

        return "dashboard";
    }
}
