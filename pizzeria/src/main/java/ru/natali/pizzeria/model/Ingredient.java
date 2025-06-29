package ru.natali.pizzeria.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Entity
@Table(name = "ingredients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "stock_quantity", nullable = false)
    private Double stockQuantity;

    @Column(nullable = false)
    private String unit;

    @Column(name = "cost_per_unit", nullable = false)
    private Double costPerUnit;

    @OneToMany(mappedBy = "ingredient", cascade = CascadeType.ALL)
    private List<PizzaIngredient> pizzas;

}