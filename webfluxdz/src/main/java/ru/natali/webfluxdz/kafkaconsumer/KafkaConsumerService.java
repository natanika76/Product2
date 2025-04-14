package ru.natali.webfluxdz.kafkaconsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import ru.natali.webfluxdz.dto.MessageDto;

@Service
public class KafkaConsumerService {

    private final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @KafkaListener(topics = "test-topic")
    public void consume(@Payload MessageDto message) {
        logger.info("Received message from Kafka: {}", message);
    }
}
