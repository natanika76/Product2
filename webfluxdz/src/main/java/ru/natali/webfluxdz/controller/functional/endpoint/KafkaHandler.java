package ru.natali.webfluxdz.controller.functional.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import ru.natali.webfluxdz.kafkaproducer.KafkaProducerService;

import java.util.List;

@Component
public class KafkaHandler {

    @Autowired
    private KafkaProducerService kafkaProducerService;

    public Mono<ServerResponse> sendMessages(ServerRequest request) {
        int count = Integer.parseInt(request.queryParam("count").orElse("10"));

        // Используем fromPublisher() для передачи Mono<List<MessageDto>>
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(kafkaProducerService.sendMessages(count), List.class);
    }
}
