package ru.natali.notes.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Note {
    private Long noteId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss") // Указываем желаемый формат
    private LocalDateTime createdAt;
    private String topic;
    private String content;

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "Note{" +
                "noteId=" + noteId +
                ", createdAt=" + createdAt.format(formatter) +
                ", topic='" + topic + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
