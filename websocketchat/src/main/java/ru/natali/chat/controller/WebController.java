package ru.natali.chat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class WebController {

    @GetMapping("/chat")
    public String chatPage(@RequestParam(required = false) String username, Model model) {
        if (username != null && !username.isEmpty()) {
            model.addAttribute("username", username);
        }
        return "chat";
    }
}
