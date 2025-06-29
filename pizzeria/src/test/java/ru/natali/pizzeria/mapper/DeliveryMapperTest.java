package ru.natali.pizzeria.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.natali.pizzeria.dto.DeliveryDTO;
import ru.natali.pizzeria.model.Delivery;
import ru.natali.pizzeria.model.Employee;
import ru.natali.pizzeria.model.Order;

import java.time.ZonedDateTime;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DeliveryMapperTest {

    @Autowired
    private DeliveryMapper deliveryMapper;

    @Test
    void testToDto() {
        Delivery delivery = new Delivery();
        delivery.setId(1L);
        delivery.setDeliveryTime(ZonedDateTime.now());
        delivery.setStatus("DELIVERED");
        delivery.setEstimatedTime(ZonedDateTime.now().plusHours(1));

        Order order = new Order();
        order.setId(10L);
        delivery.setOrder(order);

        Employee employee = new Employee();
        employee.setId(20L);
        delivery.setEmployee(employee);

        DeliveryDTO dto = deliveryMapper.toDto(delivery);

        assertEquals(delivery.getId(), dto.getId());
        assertEquals(delivery.getOrder().getId(), dto.getOrderId());
        assertEquals(delivery.getEmployee().getId(), dto.getEmployeeId());
        assertEquals(delivery.getDeliveryTime(), dto.getDeliveryTime());
        assertEquals(delivery.getStatus(), dto.getStatus());
        assertEquals(delivery.getEstimatedTime(), dto.getEstimatedTime());
    }

    @Test
    void testToEntity() {
        DeliveryDTO dto = new DeliveryDTO();
        dto.setId(1L);
        dto.setOrderId(10L);
        dto.setEmployeeId(20L);
        dto.setDeliveryTime(ZonedDateTime.now());
        dto.setStatus("DELIVERED");
        dto.setEstimatedTime(ZonedDateTime.now().plusHours(1));

        Delivery delivery = deliveryMapper.toEntity(dto);

        assertEquals(dto.getId(), delivery.getId());
        assertEquals(dto.getOrderId(), delivery.getOrder().getId());
        assertEquals(dto.getEmployeeId(), delivery.getEmployee().getId());
        assertEquals(dto.getDeliveryTime(), delivery.getDeliveryTime());
        assertEquals(dto.getStatus(), delivery.getStatus());
        assertEquals(dto.getEstimatedTime(), delivery.getEstimatedTime());
    }

    @Test
    void testUpdateDeliveryFromDto() {
        Delivery delivery = new Delivery();
        delivery.setId(1L);
        delivery.setStatus("PENDING");

        DeliveryDTO dto = new DeliveryDTO();
        dto.setStatus("DELIVERED");
        dto.setDeliveryTime(ZonedDateTime.now());

        deliveryMapper.updateDeliveryFromDto(dto, delivery);

        assertEquals(1L, delivery.getId());
        assertEquals("DELIVERED", delivery.getStatus());
        assertEquals(dto.getDeliveryTime(), delivery.getDeliveryTime());
        assertNull(delivery.getOrder());
    }
}
