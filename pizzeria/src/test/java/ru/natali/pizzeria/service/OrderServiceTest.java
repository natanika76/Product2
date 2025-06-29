package ru.natali.pizzeria.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.natali.pizzeria.dto.OrderDTO;
import ru.natali.pizzeria.dto.OrderItemDTO;
import ru.natali.pizzeria.exception.EntityNotFoundException;
import ru.natali.pizzeria.mapper.OrderMapper;
import ru.natali.pizzeria.model.Customer;
import ru.natali.pizzeria.model.Order;
import ru.natali.pizzeria.model.OrderItem;
import ru.natali.pizzeria.model.Pizza;
import ru.natali.pizzeria.repository.CustomerRepository;
import ru.natali.pizzeria.repository.OrderRepository;
import ru.natali.pizzeria.repository.PizzaRepository;
import ru.natali.pizzeria.service.kafka.KafkaProducerService;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private PizzaRepository pizzaRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private KafkaProducerService kafkaProducerService;
    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private OrderDTO orderDTO;
    private Customer customer;
    private Pizza pizza;
    private OrderItem orderItem;
    private OrderItemDTO orderItemDTO;

    @BeforeEach
    void setUp() {
        customer = new Customer();
        customer.setId(1L);
        customer.setName("Test Customer");

        pizza = new Pizza();
        pizza.setId(1L);
        pizza.setName("Margherita");
        pizza.setPrice(10.0);

        orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setPizza(pizza);
        orderItem.setQuantity(2);
        orderItem.setPrice(20.0);

        order = new Order();
        order.setId(1L);
        order.setCustomer(customer);
        order.setItems(new ArrayList<>(List.of(orderItem)));
        order.setTotalPrice(20.0);
        order.setStatus("NEW");
        order.setOrderDate(ZonedDateTime.now());

        orderItemDTO = new OrderItemDTO();
        orderItemDTO.setPizzaId(1L);
        orderItemDTO.setQuantity(2);
        orderItemDTO.setPrice(20.0);

        orderDTO = new OrderDTO();
        orderDTO.setId(1L);
        orderDTO.setCustomerId(1L);
        orderDTO.setTotalPrice(20.0);
        orderDTO.setStatus("NEW");
        orderDTO.setItems(new ArrayList<>(List.of(orderItemDTO)));
    }

    @Test
    void createOrder_ShouldSuccessfullyCreateOrder() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(pizzaRepository.findById(1L)).thenReturn(Optional.of(pizza));
        when(orderMapper.toEntity(orderDTO)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(orderDTO);

        OrderDTO result = orderService.createOrder(orderDTO);

        assertNotNull(result);
        assertEquals(orderDTO, result);
        verify(orderRepository).save(order);
        verify(kafkaProducerService).sendOrderCreatedEvent(order);
    }

    @Test
    void createOrder_ShouldThrowWhenCustomerNotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.createOrder(orderDTO));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void getOrderById_ShouldReturnOrderWhenExists() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderDTO);

        OrderDTO result = orderService.getOrderById(1L);

        assertEquals(orderDTO, result);
    }

    @Test
    void getOrderById_ShouldThrowWhenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> orderService.getOrderById(1L));
    }


    @Test
    void updateOrder_ShouldThrowWhenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> orderService.updateOrder(1L, orderDTO));
    }

    @Test
    void getAllOrders_ShouldReturnAllOrders() {
        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderDTO);

        List<OrderDTO> result = orderService.getAllOrders();

        assertEquals(1, result.size());
        assertEquals(orderDTO, result.get(0));
    }

    @Test
    void createOrder_ShouldThrowWhenPizzaNotFound() {

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(pizzaRepository.findById(1L)).thenReturn(Optional.empty());

        Order orderWithCustomer = new Order();
        orderWithCustomer.setCustomer(customer);
        when(orderMapper.toEntity(orderDTO)).thenReturn(orderWithCustomer);

        assertThrows(EntityNotFoundException.class, () -> orderService.createOrder(orderDTO));
        verify(orderRepository, never()).save(any());
    }

    @Test
    void createOrder_ShouldSetCurrentDateWhenNotProvided() {

        orderDTO.setOrderDate(null);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(pizzaRepository.findById(1L)).thenReturn(Optional.of(pizza));
        when(orderMapper.toEntity(orderDTO)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);

        OrderDTO expectedDto = new OrderDTO();
        expectedDto.setOrderDate(ZonedDateTime.now());
        when(orderMapper.toDto(order)).thenReturn(expectedDto);

        OrderDTO result = orderService.createOrder(orderDTO);

        assertNotNull(result);
        assertNotNull(result.getOrderDate());
        verify(orderRepository).save(order);
    }

    @Test
    void updateOrder_ShouldSendItemsUpdatedEventWhenItemsChanged() {
        OrderItemDTO changedItemDTO = new OrderItemDTO();
        changedItemDTO.setPizzaId(1L);
        changedItemDTO.setQuantity(3);
        changedItemDTO.setPrice(30.0);

        OrderDTO updatedDTO = new OrderDTO();
        updatedDTO.setItems(new ArrayList<>(List.of(changedItemDTO)));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(pizzaRepository.findById(1L)).thenReturn(Optional.of(pizza));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(updatedDTO);

        orderService.updateOrder(1L, updatedDTO);

        verify(kafkaProducerService).sendOrderItemsUpdatedEvent(eq(1L), anyList());
    }

    @Test
    void updateOrder_ShouldNotSendItemsUpdatedEventWhenItemsNotChanged() {
        OrderDTO updatedDTO = new OrderDTO();
        updatedDTO.setItems(List.of(orderItemDTO)); // Same items

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(pizzaRepository.findById(1L)).thenReturn(Optional.of(pizza));
        when(orderRepository.save(order)).thenReturn(order);
        when(orderMapper.toDto(order)).thenReturn(updatedDTO);

        orderService.updateOrder(1L, updatedDTO);

        verify(kafkaProducerService, never()).sendOrderItemsUpdatedEvent(anyLong(), anyList());
    }

    @Test
    void deleteOrder_ShouldDeleteOrder() {

        doNothing().when(orderRepository).deleteById(1L);

        orderService.deleteOrder(1L);

        verify(orderRepository).deleteById(1L);
    }

    @Test
    void deleteOrder_ShouldNotThrowWhenOrderNotExists() {

        doNothing().when(orderRepository).deleteById(1L);

        assertDoesNotThrow(() -> orderService.deleteOrder(1L));
        verify(orderRepository).deleteById(1L);
    }
}
