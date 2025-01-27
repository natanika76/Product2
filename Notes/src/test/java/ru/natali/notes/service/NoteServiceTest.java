package ru.natali.notes.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.natali.notes.model.Note;
import ru.natali.notes.repository.NoteRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
/*
Этот класс представляет собой unit-тест:

Аннотация @Mock создает мокированные объекты для зависимостей, таких как NoteRepository.
Аннотация @InjectMocks внедряет эти моки в тестируемый объект NoteService.
В этом тесте зависимость NoteRepository заменена моком, что позволяет изолировать
тестирование NoteService от реальной базы данных.
Тест проверяет, что метод createNote в NoteService корректно вызывает метод create
 в моке NoteRepository с правильными параметрами.
 */
class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @InjectMocks
    private NoteService noteService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateNote() {
        // Arrange
        String topic = "Test Topic";
        String content = "Test Content";
        LocalDateTime now = LocalDateTime.now();

        doNothing().when(noteRepository).create(any());

        // Act
        noteService.createNote(topic, content);

        // Assert
        ArgumentCaptor<Note> captor = ArgumentCaptor.forClass(Note.class);
        verify(noteRepository).create(captor.capture());
        Note capturedNote = captor.getValue();

        assertEquals(topic, capturedNote.getTopic());
        assertEquals(content, capturedNote.getContent());
        assertTrue(capturedNote.getCreatedAt().isAfter(now.minusSeconds(1)) &&
                capturedNote.getCreatedAt().isBefore(now.plusSeconds(1)));
    }

    @Test
    void testGetNoteById() {
        // Arrange
        Long noteId = 1L;
        Note expectedNote = new Note();
        expectedNote.setNoteId(noteId);
        expectedNote.setTopic("Test Topic");
        expectedNote.setContent("Test Content");
        expectedNote.setCreatedAt(LocalDateTime.now());

        when(noteRepository.findById(noteId)).thenReturn(expectedNote);

        // Act
        Note actualNote = noteService.getNoteById(noteId);

        // Assert
        assertEquals(expectedNote, actualNote);
    }

    @Test
    void testGetAllNotes() {
        // Arrange
        List<Note> expectedNotes = new ArrayList<>();
        expectedNotes.add(new Note());
        expectedNotes.add(new Note());

        when(noteRepository.findAll()).thenReturn(expectedNotes);

        // Act
        List<Note> actualNotes = noteService.getAllNotes();

        // Assert
        assertEquals(expectedNotes, actualNotes);
    }

    @Test
    void testUpdateNote() {
        // Arrange
        Long noteId = 1L;
        String updatedTopic = "Updated Topic";
        String updatedContent = "Updated Content";
        Note existingNote = new Note();
        existingNote.setNoteId(noteId);
        existingNote.setTopic("Old Topic");
        existingNote.setContent("Old Content");
        existingNote.setCreatedAt(LocalDateTime.now());

        when(noteRepository.findById(noteId)).thenReturn(existingNote);

        // Act
        noteService.updateNote(noteId, updatedTopic, updatedContent);

        // Assert
        verify(noteRepository).update(argThat((Note note) ->
                note.getNoteId().equals(noteId)
                        && note.getTopic().equals(updatedTopic)
                        && note.getContent().equals(updatedContent)));
    }

    @Test
    void testDeleteNote() {
        // Arrange
        Long noteId = 1L;

        // Act
        noteService.deleteNote(noteId);

        // Assert
        verify(noteRepository).deleteById(noteId);
    }
}
