package ru.natali.pizzeria.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.natali.pizzeria.dto.OrderItemDTO;
import ru.natali.pizzeria.model.OrderItem;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    @Mapping(target = "pizzaId", source = "pizza.id")
    OrderItemDTO toDto(OrderItem orderItem);

    @Mapping(target = "pizza", ignore = true) // Устанавливается вручную
    @Mapping(target = "order", ignore = true) // Устанавливается вручную
    OrderItem toEntity(OrderItemDTO orderItemDTO);
}