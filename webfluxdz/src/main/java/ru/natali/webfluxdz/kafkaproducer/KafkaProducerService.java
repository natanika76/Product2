package ru.natali.webfluxdz.kafkaproducer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.natali.webfluxdz.dto.MessageDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class KafkaProducerService {

    private final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);

    @Autowired
    private ReactiveKafkaProducerTemplate<String, MessageDto> reactiveKafkaProducerTemplate;

    /**
     * Метод для отправки нескольких сообщений в Kafka.
     *
     * @param count Количество сообщений для отправки
     */
    public Mono<List<MessageDto>> sendMessages(int count) {
        return Flux.range(1, count)
                .map(i -> MessageDto.builder()
                        .id((long) i)
                        .message("Test message " + i)
                        .sendTime(LocalDateTime.now())
                        .build())
                .flatMap(message -> {
                    logger.info("Sending message: {}", message);  // Логируем перед отправкой
                    return reactiveKafkaProducerTemplate.send("test-topic", message)
                            .doOnError(e -> logger.error("Send failed: ", e))  // Логируем ошибки
                            .thenReturn(message);
                })
                .collectList();
    }
}
