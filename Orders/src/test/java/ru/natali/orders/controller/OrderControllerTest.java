package ru.natali.orders.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import ru.natali.orders.dto.OrderRequest;
import ru.natali.orders.model.Order;
import ru.natali.orders.repository.OrderRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class OrderControllerTest {
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderController orderController;

    public OrderControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateOrder() {
        // Подготовка данных
        OrderRequest request = new OrderRequest();
        request.setProductArticle("ART12345");
        request.setQuantity(2);
        request.setTotalAmount(new BigDecimal("1999.98"));

        Order savedOrder = new Order();
        savedOrder.setOrderId(1L);
        savedOrder.setProductArticle(request.getProductArticle());
        savedOrder.setQuantity(request.getQuantity());
        savedOrder.setTotalAmount(request.getTotalAmount());
        savedOrder.setOrderDate(LocalDateTime.now());

        // Мокируем вызов репозитория
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder.getOrderId());

        // Вызов метода контроллера
        ResponseEntity<Order> response = orderController.createOrder(request);

        // Проверка результата
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(savedOrder.getOrderId(), response.getBody().getOrderId());
    }

}