package ru.natali.medregistry.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DoctorEvent {
    private String eventType; // CREATE, UPDATE, DELETE
    private Object payload;

    // Пустой конструктор обязателен для десериализации
    public DoctorEvent() {
    }

    public DoctorEvent(String eventType, Object payload) {
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
}