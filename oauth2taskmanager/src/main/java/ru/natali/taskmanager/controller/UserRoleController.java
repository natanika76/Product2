package ru.natali.taskmanager.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRoleController {
    @GetMapping("/user-role")
    public String getUserRole(org.springframework.security.core.Authentication authentication) {
        if (authentication != null) {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
            return isAdmin ? "ADMIN" : "USER";
        }
        return "USER";
    }
}
