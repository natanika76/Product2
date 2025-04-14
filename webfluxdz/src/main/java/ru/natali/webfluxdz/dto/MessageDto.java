package ru.natali.webfluxdz.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

// класс будет использоваться для сериализации и десериализации сообщений JSON в Kafka
@Data
@Builder
public class MessageDto {
    private Long id;
    private String message;
    private LocalDateTime sendTime;

    public MessageDto() {
    }

    public MessageDto(Long id, String message, LocalDateTime sendTime) {
        this.id = id;
        this.message = message;
        this.sendTime = sendTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }
}
