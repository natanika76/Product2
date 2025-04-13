package ru.natali.chat.dto;

import lombok.Data;

@Data
public class ChatMessageDTO {
    private String content;
    private String sender;
    private String type;  // CHAT, JOIN, LEAVE
    private String roomId;

    // Добавьте конструкторы если нужно
    public ChatMessageDTO() {}
}
