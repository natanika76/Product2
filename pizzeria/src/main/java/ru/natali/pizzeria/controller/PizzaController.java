package ru.natali.pizzeria.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.natali.pizzeria.dto.PizzaDTO;
import ru.natali.pizzeria.service.PizzaService;

import java.util.List;

@RestController
@RequestMapping("/api/pizzas")
@RequiredArgsConstructor
public class PizzaController {
    private final PizzaService pizzaService;

    @GetMapping
    public ResponseEntity<List<PizzaDTO>> getAllPizzas() {
        return ResponseEntity.ok(pizzaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PizzaDTO> getPizzaById(@PathVariable Long id) {
        return ResponseEntity.ok(pizzaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PizzaDTO> createPizza(@RequestBody PizzaDTO pizzaDTO) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(pizzaService.create(pizzaDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PizzaDTO> updatePizza(
            @PathVariable Long id,
            @RequestBody PizzaDTO pizzaDTO) {
        return ResponseEntity.ok(pizzaService.update(id, pizzaDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePizza(@PathVariable Long id) {
        pizzaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}