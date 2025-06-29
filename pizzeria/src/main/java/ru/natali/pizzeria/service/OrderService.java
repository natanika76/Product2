package ru.natali.pizzeria.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.natali.pizzeria.dto.OrderDTO;
import ru.natali.pizzeria.dto.OrderItemDTO;
import ru.natali.pizzeria.dto.OrderItemEvent;
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
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CustomerRepository customerRepository;
    private final PizzaRepository pizzaRepository;
    private final KafkaProducerService kafkaProducer;

    public OrderDTO createOrder(OrderDTO orderDTO) {

        Customer customer = customerRepository.findById(orderDTO.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer", orderDTO.getCustomerId()));

        Order order = orderMapper.toEntity(orderDTO);
        order.setCustomer(customer);

        if (order.getOrderDate() == null) {
            order.setOrderDate(ZonedDateTime.now());
        }

        if (orderDTO.getItems() != null) {
            List<OrderItem> items = orderDTO.getItems().stream()
                    .map(itemDto -> {
                        // Получаем пиццу
                        Pizza pizza = pizzaRepository.findById(itemDto.getPizzaId())
                                .orElseThrow(() -> new EntityNotFoundException("Pizza", itemDto.getPizzaId()));

                        OrderItem item = new OrderItem();
                        item.setPizza(pizza);
                        item.setQuantity(itemDto.getQuantity());
                        item.setPrice(itemDto.getPrice());
                        item.setSpecialRequests(itemDto.getSpecialRequests());
                        item.setOrder(order);

                        return item;
                    })
                    .collect(Collectors.toList());

            order.setItems(items);
        }

        Order savedOrder = orderRepository.save(order);

        kafkaProducer.sendOrderCreatedEvent(savedOrder);

        return orderMapper.toDto(savedOrder);
    }

    public OrderDTO getOrderById(Long id) {
        return orderRepository.findById(id)
                .map(orderMapper::toDto)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::toDto)
                .toList();
    }

    public OrderDTO updateOrder(Long id, OrderDTO orderDTO) {
        Order existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order", id));

        orderMapper.updateOrderFromDto(orderDTO, existingOrder);

        if (orderDTO.getItems() != null) {
            updateOrderItems(existingOrder, orderDTO.getItems());
        }

        Order updatedOrder = orderRepository.save(existingOrder);

        if (!existingOrder.getStatus().equals(updatedOrder.getStatus())) {
            kafkaProducer.sendOrderStatusChangedEvent(id, updatedOrder.getStatus());
        }

        return orderMapper.toDto(updatedOrder);
    }

    private void updateOrderItems(Order order, List<OrderItemDTO> itemDTOs) {

        List<OrderItem> originalItems = new ArrayList<>(order.getItems());

        order.getItems().clear();

        itemDTOs.forEach(itemDTO -> {
            Pizza pizza = pizzaRepository.findById(itemDTO.getPizzaId())
                    .orElseThrow(() -> new EntityNotFoundException("Pizza", itemDTO.getPizzaId()));

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setPizza(pizza);
            item.setQuantity(itemDTO.getQuantity());
            item.setPrice(itemDTO.getPrice());
            item.setSpecialRequests(itemDTO.getSpecialRequests());

            order.getItems().add(item);
        });

        if (itemsChanged(originalItems, order.getItems())) {
            kafkaProducer.sendOrderItemsUpdatedEvent(
                    order.getId(),
                    order.getItems().stream()
                            .map(i -> new OrderItemEvent(
                                    i.getPizza().getId(),
                                    i.getPizza().getName(),
                                    i.getQuantity(),
                                    i.getPrice()
                            ))
                            .collect(Collectors.toList())
            );
        }
    }

    private boolean itemsChanged(List<OrderItem> oldItems, List<OrderItem> newItems) {
        if (oldItems.size() != newItems.size()) return true;

        Map<Long, Integer> oldQuantities = oldItems.stream()
                .collect(Collectors.toMap(
                        item -> item.getPizza().getId(),
                        OrderItem::getQuantity
                ));

        for (OrderItem newItem : newItems) {
            Long pizzaId = newItem.getPizza().getId();
            if (!oldQuantities.containsKey(pizzaId) ||
                    !oldQuantities.get(pizzaId).equals(newItem.getQuantity())) {
                return true;
            }
        }

        return false;
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
    }
}