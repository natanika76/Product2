package ru.natali.chat.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.natali.chat.dto.ChatMessageDTO;
import ru.natali.chat.model.ChatMessage;
import ru.natali.chat.model.User;
import ru.natali.chat.repository.ChatMessageRepository;
import ru.natali.chat.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class ChatService {
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;

    public ChatService(ChatMessageRepository chatMessageRepository, UserRepository userRepository) {
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
    }

    public ChatMessage saveMessage(ChatMessageDTO chatMessageDTO) {
        User user = userRepository.findByUsername(chatMessageDTO.getSender());

        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContent(chatMessageDTO.getContent());
        chatMessage.setUser(user);
        chatMessage.setTimestamp(LocalDateTime.now());
        chatMessage.setRoomId(chatMessageDTO.getRoomId());

        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> getChatHistory(String roomId) {
        return chatMessageRepository.findByRoomIdOrderByTimestampAsc(roomId);
    }
}
