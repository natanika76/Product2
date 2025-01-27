package ru.natali.notes.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ru.natali.notes.model.Note;
import ru.natali.notes.repository.NoteRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
/*
Unit-тесты для быстрой проверки логики компонентов.
Component-тесты для проверки интеграции с реальными зависимостями, такими как база данных.

Этот класс представляет собой component-тест (интеграционное тестирование)
Аннотация @SpringBootTest запускает весь контекст Spring Boot, включая все бины и зависимости,
 такие как NoteService и NoteRepository.
В этом тесте используется настоящий экземпляр NoteRepository, который взаимодействует с
встроенной тестовой базой данных.
Перед каждым тестом и после него таблица очищается с помощью метода deleteAll(), чтобы обеспечить чистоту данных.
Тест проверяет интеграцию между сервисом и репозиторием, работая с реальной базой данных.
 */
@SpringBootTest
@ActiveProfiles("test")
public class NoteServiceComponentTest {

    @Autowired
    private NoteService noteService;

    @Autowired
    private NoteRepository noteRepository;

    @BeforeEach
    void setUp() {
        // Очищаем таблицу перед каждым тестом
        noteRepository.deleteAll();
    }

    @AfterEach
    void tearDown() {
        // Очищаем таблицу после каждого теста
        noteRepository.deleteAll();
    }

    @Test
    void testCreateAndGetNote() {
        // Arrange
        String topic = "Test Topic";
        String content = "Test Content";

        // Act
        noteService.createNote(topic, content);
        List<Note> notes = noteService.getAllNotes();

        // Assert
        assertThat(notes).hasSize(1);
        assertThat(notes.get(0).getTopic()).isEqualTo(topic);
        assertThat(notes.get(0).getContent()).isEqualTo(content);
    }

    @Test
    void testGetNoteById() {
        // Arrange
        String topic = "Test Topic";
        String content = "Test Content";
        noteService.createNote(topic, content);
        List<Note> notes = noteService.getAllNotes();
        long noteId = notes.get(0).getNoteId();

        // Act
        Note retrievedNote = noteService.getNoteById(noteId);

        // Assert
        assertThat(retrievedNote).isNotNull();
        assertThat(retrievedNote.getTopic()).isEqualTo(topic);
        assertThat(retrievedNote.getContent()).isEqualTo(content);
    }

    @Test
    void testUpdateNote() {
        // Arrange
        String initialTopic = "Initial Topic";
        String initialContent = "Initial Content";
        noteService.createNote(initialTopic, initialContent);
        List<Note> notes = noteService.getAllNotes();
        long noteId = notes.get(0).getNoteId();

        String updatedTopic = "Updated Topic";
        String updatedContent = "Updated Content";

        // Act
        noteService.updateNote(noteId, updatedTopic, updatedContent);
        Note updatedNote = noteService.getNoteById(noteId);

        // Assert
        assertThat(updatedNote).isNotNull();
        assertThat(updatedNote.getTopic()).isEqualTo(updatedTopic);
        assertThat(updatedNote.getContent()).isEqualTo(updatedContent);
    }

    @Test
    void testDeleteNote() {
        // Arrange
        String topic = "Test Topic";
        String content = "Test Content";
        noteService.createNote(topic, content);
        List<Note> notes = noteService.getAllNotes();
        long noteId = notes.get(0).getNoteId();

        // Act
        noteService.deleteNote(noteId);
        List<Note> remainingNotes = noteService.getAllNotes();

        // Assert
        assertThat(remainingNotes).isEmpty();
    }

    @Test
    void testGetAllNotesMultipleEntries() {
        // Arrange
        String topic1 = "First Note";
        String content1 = "First Content";
        noteService.createNote(topic1, content1);

        String topic2 = "Second Note";
        String content2 = "Second Content";
        noteService.createNote(topic2, content2);

        // Act
        List<Note> allNotes = noteService.getAllNotes();

        // Assert
        assertThat(allNotes).hasSize(2);
        assertThat(allNotes.get(0).getTopic()).isEqualTo(topic1);
        assertThat(allNotes.get(0).getContent()).isEqualTo(content1);
        assertThat(allNotes.get(1).getTopic()).isEqualTo(topic2);
        assertThat(allNotes.get(1).getContent()).isEqualTo(content2);
    }
}