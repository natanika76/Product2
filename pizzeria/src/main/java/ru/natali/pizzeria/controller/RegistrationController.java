package ru.natali.pizzeria.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.natali.pizzeria.dto.UserDTO;
import ru.natali.pizzeria.service.UserService;

import java.util.Set;

@Controller
@RequiredArgsConstructor
public class RegistrationController {

    private final UserService userService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("userDto", new UserDTO());
        return "registration";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("userDto") UserDTO userDto,
                               @RequestParam String confirmPassword,
                               Model model) {

        if (!userDto.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "registration";
        }

        try {
            userDto.setRoles(Set.of("ROLE_USER"));
            userService.createUser(userDto);
            model.addAttribute("success", "Registration successful! You can now login.");
            return "redirect:/login?registered";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "registration";
        }
    }
}
