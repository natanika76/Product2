package ru.natali.pizzeria.service.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.natali.pizzeria.config.KafkaTopics;
import ru.natali.pizzeria.dto.OrderEvent;
import ru.natali.pizzeria.dto.OrderItemEvent;
import ru.natali.pizzeria.model.Order;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendOrderCreatedEvent(Order order) {
        OrderEvent event = new OrderEvent(
                order.getId(),
                order.getCustomer().getId(),
                order.getCustomer().getName(),
                order.getStatus(),
                ZonedDateTime.now(),
                order.getTotalPrice()
        );
        kafkaTemplate.send(KafkaTopics.ORDER_CREATED, event);
    }

    public void sendOrderStatusChangedEvent(Long orderId, String newStatus) {
        Map<String, Object> event = new HashMap<>();
        event.put("orderId", orderId);
        event.put("newStatus", newStatus);
        event.put("timestamp", ZonedDateTime.now().toString());

        kafkaTemplate.send(KafkaTopics.ORDER_STATUS_CHANGED, event);
    }

    public void sendOrderItemsUpdatedEvent(Long orderId, List<OrderItemEvent> items) {
        Map<String, Object> event = new HashMap<>();
        event.put("orderId", orderId);
        event.put("items", items);
        event.put("timestamp", ZonedDateTime.now().toString());

        kafkaTemplate.send("order-items-updated", event);
    }
}
