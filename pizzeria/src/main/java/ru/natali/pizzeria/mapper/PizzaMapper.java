package ru.natali.pizzeria.mapper;

import org.mapstruct.*;
import ru.natali.pizzeria.dto.PizzaDTO;
import ru.natali.pizzeria.model.Pizza;

@Mapper(componentModel = "spring", uses = {PizzaIngredientMapper.class})
public interface PizzaMapper {
    PizzaDTO toDto(Pizza pizza);

    @Mapping(target = "ingredients", ignore = true)
    Pizza toEntity(PizzaDTO pizzaDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updatePizzaFromDto(PizzaDTO dto, @MappingTarget Pizza entity);
}
