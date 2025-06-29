package ru.natali.pizzeria.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pizzas")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Pizza {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private Double price;

    private Boolean available = true;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "cooking_time_min")
    private Integer cookingTimeMin = 15;

    private String category;

    @OneToMany(mappedBy = "pizza", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<PizzaIngredient> ingredients = new ArrayList<>();
}