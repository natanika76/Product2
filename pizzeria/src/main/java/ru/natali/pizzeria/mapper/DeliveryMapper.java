package ru.natali.pizzeria.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import ru.natali.pizzeria.dto.DeliveryDTO;
import ru.natali.pizzeria.model.Delivery;

@Mapper(componentModel = "spring")
public interface DeliveryMapper {
    DeliveryMapper INSTANCE = Mappers.getMapper(DeliveryMapper.class);

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "employee.id", target = "employeeId")
    DeliveryDTO toDto(Delivery delivery);

    @Mapping(source = "orderId", target = "order.id")
    @Mapping(source = "employeeId", target = "employee.id")
    Delivery toEntity(DeliveryDTO deliveryDTO);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "employee", ignore = true)
    void updateDeliveryFromDto(DeliveryDTO deliveryDTO, @MappingTarget Delivery delivery);
}
