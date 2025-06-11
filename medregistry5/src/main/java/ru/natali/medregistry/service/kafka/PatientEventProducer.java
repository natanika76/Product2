package ru.natali.medregistry.service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.natali.medregistry.dto.PatientDTO;
import ru.natali.medregistry.model.PatientEvent;

@Service
@Slf4j
public class PatientEventProducer {

    private final KafkaTemplate<String, PatientEvent> kafkaTemplate;

    public PatientEventProducer(KafkaTemplate<String, PatientEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendPatientCreatedEvent(PatientDTO patientDTO) {
        PatientEvent event = new PatientEvent("CREATE", patientDTO);
        log.info("Отправка события создания пациента: {}", event);
        kafkaTemplate.send("patient-events", event);
    }

    public void sendPatientUpdatedEvent(PatientDTO patientDTO) {
        PatientEvent event = new PatientEvent("UPDATE", patientDTO);
        log.info("Отправка события обновления пациента: {}", event);
        kafkaTemplate.send("patient-events", event);
    }

    public void sendPatientDeletedEvent(Long patientId) {
        PatientEvent event = new PatientEvent("DELETE", patientId);
        log.info("Отправка события удаления пациента: {}", event);
        kafkaTemplate.send("patient-events", event);
    }
}
