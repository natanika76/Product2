package ru.natali.pizzeria.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.natali.pizzeria.dto.OrderDTO;
import ru.natali.pizzeria.model.Order;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {
    @Mapping(target = "customerId", source = "customer.id")
    OrderDTO toDto(Order order);

    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "delivery", ignore = true)
    Order toEntity(OrderDTO orderDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "items", ignore = true)
    @Mapping(target = "delivery", ignore = true)
    void updateOrderFromDto(OrderDTO orderDTO, @MappingTarget Order order);
}