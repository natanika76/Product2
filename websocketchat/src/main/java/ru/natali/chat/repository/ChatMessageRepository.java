package ru.natali.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.natali.chat.model.ChatMessage;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByRoomIdOrderByTimestampAsc(String roomId);
}
