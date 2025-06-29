package ru.natali.pizzeria.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.natali.pizzeria.dto.OrderDTO;
import ru.natali.pizzeria.model.Customer;
import ru.natali.pizzeria.model.Order;
import ru.natali.pizzeria.model.OrderItem;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OrderMapperTest {

    @Autowired
    private OrderMapper orderMapper;

    @Test
    void testToDto() {
        Order order = new Order();
        order.setId(1L);
        order.setOrderDate(ZonedDateTime.now());
        order.setStatus("PROCESSING");
        order.setTotalPrice(49.97);
        order.setDeliveryAddress("456 Oak St");
        order.setPaymentMethod("CARD");
        order.setPaymentStatus("PAID");

        Customer customer = new Customer();
        customer.setId(10L);
        order.setCustomer(customer);

        order.setItems(Collections.emptyList());

        OrderDTO dto = orderMapper.toDto(order);

        assertEquals(order.getId(), dto.getId());
        assertEquals(order.getCustomer().getId(), dto.getCustomerId());
        assertEquals(order.getOrderDate(), dto.getOrderDate());
        assertEquals(order.getStatus(), dto.getStatus());
        assertEquals(order.getTotalPrice(), dto.getTotalPrice());
        assertEquals(order.getDeliveryAddress(), dto.getDeliveryAddress());
        assertEquals(order.getPaymentMethod(), dto.getPaymentMethod());
        assertEquals(order.getPaymentStatus(), dto.getPaymentStatus());
    }

    @Test
    void testToEntity() {
        OrderDTO dto = new OrderDTO();
        dto.setId(1L);
        dto.setCustomerId(10L);
        dto.setOrderDate(ZonedDateTime.now());
        dto.setStatus("PROCESSING");
        dto.setTotalPrice(49.97);
        dto.setDeliveryAddress("456 Oak St");
        dto.setPaymentMethod("CARD");
        dto.setPaymentStatus("PAID");
        dto.setItems(Collections.emptyList());

        Order order = orderMapper.toEntity(dto);

        assertEquals(dto.getId(), order.getId());
        assertNull(order.getCustomer());
        assertEquals(dto.getOrderDate(), order.getOrderDate());
        assertEquals(dto.getStatus(), order.getStatus());
        assertEquals(dto.getTotalPrice(), order.getTotalPrice());
        assertEquals(dto.getDeliveryAddress(), order.getDeliveryAddress());
        assertEquals(dto.getPaymentMethod(), order.getPaymentMethod());
        assertEquals(dto.getPaymentStatus(), order.getPaymentStatus());
        assertNotNull(order.getItems());
        assertEquals(0, order.getItems().size());
    }

    @Test
    void testUpdateOrderFromDto() {
        Order order = new Order();
        order.setId(1L);
        order.setStatus("NEW");
        order.setTotalPrice(10.0);
        order.setItems(Collections.emptyList());

        OrderDTO dto = new OrderDTO();
        dto.setStatus("CANCELLED");
        dto.setDeliveryAddress("789 Pine St");
        dto.setTotalPrice(20.0);

        orderMapper.updateOrderFromDto(dto, order);

        assertEquals(1L, order.getId());
        assertEquals("CANCELLED", order.getStatus());
        assertEquals("789 Pine St", order.getDeliveryAddress());
        assertEquals(20.0, order.getTotalPrice());
        assertNotNull(order.getItems());
        assertEquals(0, order.getItems().size());
        assertNull(order.getCustomer());
    }

    @Test
    void testToEntityWithNullItems() {
        OrderDTO dto = new OrderDTO();
        dto.setItems(null);

        Order order = orderMapper.toEntity(dto);

        assertNotNull(order.getItems());
        assertTrue(order.getItems().isEmpty());
    }

    @Test
    void testItemsIgnoredInMapping() {
        Order order = new Order();
        order.setItems(List.of(new OrderItem()));

        OrderDTO dto = new OrderDTO();
        dto.setItems(Collections.emptyList());

        orderMapper.updateOrderFromDto(dto, order);

        assertEquals(1, order.getItems().size());
    }
}