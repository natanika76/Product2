package ru.natali.notes.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import ru.natali.notes.model.Note;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/* Этот класс представляет собой component-тест (интеграционный тест)
Он использует реальный экземпляр JdbcTemplate, который подключается к тестовой базе данных.
Перед каждым тестом таблица очищается, чтобы гарантировать чистоту данных.
В тестовом методе testCreateNote проверяется реальная работа репозитория с базой данных:
создается новая заметка, сохраняется в базе данных, а затем проверяется, что она была успешно сохранена.
 */
@DataJdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test") /* Эта аннотация полезна в интеграционных тестах (component-тестах), где мы загружаем
 контекст Spring и хотим применить особую конфигурацию для тестирования.
*/
public class NoteRepositoryTest {

    @Autowired
    private JdbcTemplate jdbcTemplate; // Внедряем JdbcTemplate

    private NoteRepository noteRepository;

    @BeforeEach
    void setUp() {
        // Создаем NoteRepository, передавая JdbcTemplate
        noteRepository = new NoteRepository(jdbcTemplate);

        // Очищаем таблицу перед каждым тестом
        jdbcTemplate.execute("DELETE FROM notes");
    }

    @Test
    void testCreateNote() {
        // Arrange
        Note note = new Note();
        note.setCreatedAt(LocalDateTime.now());
        note.setTopic("Test Topic");
        note.setContent("Test Content");

        // Act
        noteRepository.create(note);

        // Assert
        List<Note> notes = noteRepository.findAll();
        assertThat(notes).hasSize(1); // Ожидаем только одну запись
        assertThat(notes.get(0).getTopic()).isEqualTo("Test Topic");
    }

    @Test
    void testFindAll() {
        // Arrange
        Note note = new Note();
        note.setCreatedAt(LocalDateTime.now());
        note.setTopic("Test Topic");
        note.setContent("Test Content");
        noteRepository.create(note);

        // Act
        List<Note> notes = noteRepository.findAll();

        // Assert
        assertThat(notes).hasSize(1);
        assertThat(notes.get(0).getTopic()).isEqualTo("Test Topic");
    }

    @Test
    void testFindById() {
        // Arrange
        Note note = new Note();
        note.setCreatedAt(LocalDateTime.now());
        note.setTopic("Test Topic");
        note.setContent("Test Content");
        noteRepository.create(note);

        // Получаем ID созданной заметки
        List<Note> notes = noteRepository.findAll();
        assertThat(notes).hasSize(1); // Убеждаемся, что заметка создана
        Long noteId = notes.get(0).getNoteId();

        // Act
        Note foundNote = noteRepository.findById(noteId);

        // Assert
        assertThat(foundNote).isNotNull();
        assertThat(foundNote.getTopic()).isEqualTo("Test Topic");
    }

    @Test
    void testUpdateNote() {
        // Arrange
        Note note = new Note();
        note.setCreatedAt(LocalDateTime.now());
        note.setTopic("Test Topic");
        note.setContent("Test Content");
        noteRepository.create(note);

        // Получаем ID созданной заметки
        List<Note> notes = noteRepository.findAll();
        assertThat(notes).hasSize(1); // Убеждаемся, что заметка создана
        Long noteId = notes.get(0).getNoteId();

        // Обновляем заметку
        Note foundNote = noteRepository.findById(noteId);
        assertThat(foundNote).isNotNull(); // Убеждаемся, что заметка найдена

        foundNote.setTopic("Updated Topic");
        foundNote.setContent("Updated Content");

        // Act
        noteRepository.update(foundNote);

        // Assert
        Note updatedNote = noteRepository.findById(noteId);
        assertThat(updatedNote).isNotNull();
        assertThat(updatedNote.getTopic()).isEqualTo("Updated Topic");
        assertThat(updatedNote.getContent()).isEqualTo("Updated Content");
    }

    @Test
    void testDeleteById() {
        // Arrange
        Note note = new Note();
        note.setCreatedAt(LocalDateTime.now());
        note.setTopic("Test Topic");
        note.setContent("Test Content");
        noteRepository.create(note);

        // Получаем ID созданной заметки
        List<Note> notesBeforeDelete = noteRepository.findAll();
        assertThat(notesBeforeDelete).hasSize(1); // Убеждаемся, что заметка создана
        Long noteId = notesBeforeDelete.get(0).getNoteId();

        // Act
        noteRepository.deleteById(noteId); // Удаляем заметку по её реальному ID
        List<Note> notesAfterDelete = noteRepository.findAll();

        // Assert
        assertThat(notesAfterDelete).isEmpty(); // Убеждаемся, что заметка удалена
    }
}