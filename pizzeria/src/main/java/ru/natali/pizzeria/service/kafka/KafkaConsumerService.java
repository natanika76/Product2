package ru.natali.pizzeria.service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.natali.pizzeria.config.KafkaTopics;
import ru.natali.pizzeria.dto.OrderEvent;

import java.util.Map;

@Service
@Slf4j
public class KafkaConsumerService {

    @KafkaListener(topics = KafkaTopics.ORDER_CREATED, groupId = "notification-group")
    public void consumeOrderCreated(OrderEvent event) {
        log.info("Received Order Created Event: {}", event);

    }

    @KafkaListener(topics = KafkaTopics.ORDER_STATUS_CHANGED, groupId = "notification-group")
    public void consumeOrderStatusChanged(@Payload Map<String, Object> event) {
        log.info("Order status changed: {}", event);

    }

    @KafkaListener(topics = "order-items-updated", groupId = "inventory-group")
    public void consumeOrderItemsUpdated(@Payload Map<String, Object> event) {
        log.info("Order items updated: {}", event);
    }
}
