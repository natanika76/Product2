package ru.natali.pizzeria.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.natali.pizzeria.dto.OrderItemDTO;
import ru.natali.pizzeria.model.OrderItem;
import ru.natali.pizzeria.model.Pizza;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderItemMapperTest {

    @Autowired
    private OrderItemMapper orderItemMapper;

    @Test
    void testToDto() {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setQuantity(2);
        orderItem.setPrice(19.99);
        orderItem.setSpecialRequests("No onions");

        Pizza pizza = new Pizza();
        pizza.setId(5L);
        orderItem.setPizza(pizza);

        OrderItemDTO dto = orderItemMapper.toDto(orderItem);

        assertEquals(orderItem.getId(), dto.getId());
        assertEquals(orderItem.getQuantity(), dto.getQuantity());
        assertEquals(orderItem.getPrice(), dto.getPrice());
        assertEquals(orderItem.getSpecialRequests(), dto.getSpecialRequests());
        assertEquals(orderItem.getPizza().getId(), dto.getPizzaId());
    }

    @Test
    void testToEntity() {
        OrderItemDTO dto = new OrderItemDTO();
        dto.setId(1L);
        dto.setPizzaId(5L);
        dto.setQuantity(2);
        dto.setPrice(19.99);
        dto.setSpecialRequests("No onions");

        OrderItem orderItem = orderItemMapper.toEntity(dto);

        assertEquals(dto.getId(), orderItem.getId());
        assertEquals(dto.getQuantity(), orderItem.getQuantity());
        assertEquals(dto.getPrice(), orderItem.getPrice());
        assertEquals(dto.getSpecialRequests(), orderItem.getSpecialRequests());
        assertNull(orderItem.getPizza());
        assertNull(orderItem.getOrder());
    }
}
