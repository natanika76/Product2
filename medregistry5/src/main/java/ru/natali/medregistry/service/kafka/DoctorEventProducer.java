package ru.natali.medregistry.service.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.natali.medregistry.dto.DoctorDTO;
import ru.natali.medregistry.model.DoctorEvent;

@Service
public class DoctorEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate; // Изменили на Object для поддержки разных типов событий

    public DoctorEventProducer(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendDoctorCreatedEvent(DoctorDTO doctorDTO) {
        kafkaTemplate.send("doctor-events", new DoctorEvent("CREATE", doctorDTO));
    }

    public void sendDoctorUpdatedEvent(DoctorDTO doctorDTO) {
        kafkaTemplate.send("doctor-events", new DoctorEvent("UPDATE", doctorDTO));
    }

    public void sendDoctorDeletedEvent(Long doctorId) {
        kafkaTemplate.send("doctor-events", new DoctorEvent("DELETE", doctorId));
    }
}