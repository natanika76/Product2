package ru.natali.medregistry.service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import ru.natali.medregistry.dto.PatientDTO;
import ru.natali.medregistry.model.PatientEvent;

@Service
@Slf4j
public class PatientEventConsumer {

    @KafkaListener(topics = "patient-events", groupId = "medregistry-group")
    public void consumePatientEvent(PatientEvent event) {
        try {
            log.info("Получено событие типа: {}", event.getEventType());

            switch (event.getEventType()) {
                case "CREATE":
                case "UPDATE":
                    // Для CREATE/UPDATE получаем полный DTO
                    PatientDTO patientDTO = event.getPatientDTO();
                    log.info("{} пациент: {}",
                            event.getEventType().equals("CREATE") ? "Создан" : "Обновлен",
                            patientDTO);
                    break;
                case "DELETE":
                    // Для DELETE получаем только ID
                    Long patientId = event.getPatientId();
                    log.info("Удален пациент с ID: {}", patientId);
                    break;
                default:
                    log.warn("Неизвестный тип события: {}", event.getEventType());
            }
        } catch (Exception e) {
            log.error("Ошибка обработки события", e);
            // Здесь можно добавить логику повторной обработки или уведомления об ошибке
        }
    }
}
