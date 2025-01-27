package ru.natali.notes.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
/*
Класс Note является простой моделью данных (POJO),
поэтому тестирование этого класса будет минимальным
 */
class NoteTest {

    @Test
    void testGettersAndSetters() {
        Note note = new Note();

        long id = 2L;
        note.setNoteId(id);
        assertThat(note.getNoteId()).isEqualTo(id);

        LocalDateTime now = LocalDateTime.now();
        note.setCreatedAt(now);
        assertThat(note.getCreatedAt()).isEqualTo(now);

        String topic = "Тестовая заметка";
        note.setTopic(topic);
        assertThat(note.getTopic()).isEqualTo(topic);

        String content = "Это тестовый контент.";
        note.setContent(content);
        assertThat(note.getContent()).isEqualTo(content);
    }
}