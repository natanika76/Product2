package ru.natali.pizzeria.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.natali.pizzeria.dto.PizzaIngredientDTO;
import ru.natali.pizzeria.model.PizzaIngredient;

@Mapper(componentModel = "spring")
public interface PizzaIngredientMapper {
    @Mapping(target = "ingredientId", source = "ingredient.id")
    PizzaIngredientDTO toDto(PizzaIngredient pizzaIngredient);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pizza", ignore = true)
    @Mapping(target = "ingredient", ignore = true)
    PizzaIngredient toEntity(PizzaIngredientDTO dto);
}
