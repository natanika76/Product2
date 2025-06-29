package ru.natali.pizzeria.controller;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.natali.pizzeria.service.PizzaService;

@Controller
@RequestMapping("/menu")
public class MenuController {
    private final PizzaService pizzaService;

    public MenuController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @GetMapping
    public String showMenu(Model model) {
        model.addAttribute("pizzas", pizzaService.getAvailablePizzas());
        return "menu";
    }
}
