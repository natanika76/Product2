package ru.natali.medregistry.service.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import ru.natali.medregistry.model.DoctorEvent;

@Service
@Slf4j
public class DoctorEventConsumer {

    @KafkaListener(topics = "doctor-events", groupId = "medregistry-group")
    public void consumeDoctorEvent(DoctorEvent event) {
        switch (event.getEventType()) {
            case "CREATE":
                log.info("Получено событие о создании врача: {}", event.getPayload());
                break;
            case "UPDATE":
                log.info("Получено событие об обновлении врача: {}", event.getPayload());
                break;
            case "DELETE":
                log.info("Получено событие об удалении врача с ID: {}", event.getPayload());
                break;
            default:
                log.warn("Получено событие с неизвестным типом: {}", event.getEventType());
        }
    }
}