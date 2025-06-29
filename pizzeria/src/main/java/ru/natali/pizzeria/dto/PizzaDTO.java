package ru.natali.pizzeria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PizzaDTO {
    private Long id;
    @NotBlank
    private String name;
    private String description;
    @Positive
    private Double price;
    private Boolean available;
    private String imageUrl;
    @PositiveOrZero
    private Integer cookingTimeMin;
    private String category;
    private List<PizzaIngredientDTO> ingredients;
}

