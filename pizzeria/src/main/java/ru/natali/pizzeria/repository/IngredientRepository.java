package ru.natali.pizzeria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.natali.pizzeria.model.Ingredient;

import java.util.List;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
    List<Ingredient> findByNameContainingIgnoreCase(String name);
}