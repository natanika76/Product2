package ru.natali.notes.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.natali.notes.model.Note;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
/*
Этот класс представляет собой unit-тест:
Он использует мокированный экземпляр JdbcTemplate, что означает, что вместо
реального подключения к базе данных мы симулируем поведение JdbcTemplate с помощью моков.
В тестовом методе testCreateNote проверяется, что метод create репозитория правильно
вызывает метод update мока JdbcTemplate с нужными параметрами.
Недостатки: не проверяет реальное взаимодействие с базой данных, поэтому возможны ошибки
конфигурацииили запросов, которые не будут обнаружены.
 */
@ExtendWith(MockitoExtension.class)/* Над unit-тестами аннотацию @ActiveProfiles("test") ставить не нужно,
так как unit-тесты работают вне контекста Spring и не требуют применения профилей.*/
public class NoteRepositoryUnitTest {

    @Mock
    private JdbcTemplate jdbcTemplate; // Мок JdbcTemplate

    @InjectMocks
    private NoteRepository noteRepository; // NoteRepository с внедренным моком JdbcTemplate

    private Note note;

    @BeforeEach
    void setUp() {
        // Подготовка тестовых данных
        note = new Note();
        note.setNoteId(1L);
        note.setCreatedAt(LocalDateTime.now());
        note.setTopic("Test Topic");
        note.setContent("Test Content");
    }

    @Test
    void testCreateNote() {
        // Arrange
        when(jdbcTemplate.update(
                eq("INSERT INTO notes (created_at, topic, content) VALUES (?, ?, ?)"),
                eq(note.getCreatedAt()),
                eq(note.getTopic()),
                eq(note.getContent())
        )).thenReturn(1);

        // Act
        noteRepository.create(note);

        // Assert
        verify(jdbcTemplate, times(1)).update(
                eq("INSERT INTO notes (created_at, topic, content) VALUES (?, ?, ?)"),
                eq(note.getCreatedAt()),
                eq(note.getTopic()),
                eq(note.getContent())
        );
    }

    @Test
    void testFindAll() {
        // Arrange
        when(jdbcTemplate.query(
                eq("SELECT * FROM notes"),
                any(RowMapper.class)
        )).thenReturn(Collections.singletonList(note));

        // Act
        List<Note> notes = noteRepository.findAll();

        // Assert
        assertThat(notes).hasSize(1);
        assertThat(notes.get(0).getTopic()).isEqualTo("Test Topic");
        verify(jdbcTemplate, times(1)).query(
                eq("SELECT * FROM notes"),
                any(RowMapper.class)
        );
    }

    @Test
    void testFindById() {
        // Arrange
        when(jdbcTemplate.queryForObject(
                eq("SELECT * FROM notes WHERE note_id = ?"),
                any(RowMapper.class),
                eq(1L)
        )).thenReturn(note);

        // Act
        Note foundNote = noteRepository.findById(1L);

        // Assert
        assertThat(foundNote).isNotNull();
        assertThat(foundNote.getTopic()).isEqualTo("Test Topic");
        verify(jdbcTemplate, times(1)).queryForObject(
                eq("SELECT * FROM notes WHERE note_id = ?"),
                any(RowMapper.class),
                eq(1L)
        );
    }

    @Test
    void testUpdateNote() {
        // Arrange
        when(jdbcTemplate.update(
                eq("UPDATE notes SET created_at = ?, topic = ?, content = ? WHERE note_id = ?"),
                eq(note.getCreatedAt()),
                eq(note.getTopic()),
                eq(note.getContent()),
                eq(note.getNoteId())
        )).thenReturn(1);

        // Act
        noteRepository.update(note);

        // Assert
        verify(jdbcTemplate, times(1)).update(
                eq("UPDATE notes SET created_at = ?, topic = ?, content = ? WHERE note_id = ?"),
                eq(note.getCreatedAt()),
                eq(note.getTopic()),
                eq(note.getContent()),
                eq(note.getNoteId())
        );
    }

    @Test
    void testDeleteById() {
        // Arrange
        when(jdbcTemplate.update(
                eq("DELETE FROM notes WHERE note_id = ?"),
                eq(1L)
        )).thenReturn(1);

        // Act
        noteRepository.deleteById(1L);

        // Assert
        verify(jdbcTemplate, times(1)).update(
                eq("DELETE FROM notes WHERE note_id = ?"),
                eq(1L)
        );
    }
}
