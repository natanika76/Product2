package ru.natali.pizzeria.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pizza_ingredients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PizzaIngredient {
    @EmbeddedId
    private PizzaIngredientId id = new PizzaIngredientId(); // Важно инициализировать!

    @ManyToOne
    @MapsId("pizzaId")
    @JoinColumn(name = "pizza_id")
    private Pizza pizza;

    @ManyToOne
    @MapsId("ingredientId")
    @JoinColumn(name = "ingredient_id")
    private Ingredient ingredient;

    @Column(nullable = false)
    private Double quantity;

}



