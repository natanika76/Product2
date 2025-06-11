package ru.natali.medregistry.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ru.natali.medregistry.dto.PatientDTO;

public class PatientEvent {
    private String eventType; // CREATE, UPDATE, DELETE
    private Object payload;

    public PatientEvent() {
        // Пустой конструктор для десериализации
    }

    public PatientEvent(String eventType, Object payload) {
        this.eventType = eventType;
        this.payload = payload;
    }

    // Геттеры и сеттеры
    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
    }

    @Override
    public String toString() {
        return "PatientEvent{" +
                "eventType='" + eventType + '\'' +
                ", payload=" + payload +
                '}';
    }

    public boolean isDeleteEvent() {
        return "DELETE".equals(eventType);
    }

    @JsonIgnore
    public PatientDTO getPatientDTO() {
        if (isDeleteEvent()) {
            throw new IllegalStateException("DELETE event contains patient ID, not PatientDTO!");
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            return mapper.convertValue(payload, PatientDTO.class);
        } catch (Exception e) {
            throw new RuntimeException("Error converting payload to PatientDTO", e);
        }
    }

    @JsonIgnore
    public Long getPatientId() {
        if (!isDeleteEvent()) {
            throw new IllegalStateException("Only DELETE event contains patient ID!");
        }
        if (payload instanceof Long) {
            return (Long) payload;
        }
        if (payload instanceof Integer) {
            return ((Integer) payload).longValue();
        }
        return Long.parseLong(payload.toString());
    }
}
