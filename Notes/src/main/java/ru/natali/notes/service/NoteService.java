package ru.natali.notes.service;

import org.springframework.stereotype.Service;
import ru.natali.notes.model.Note;
import ru.natali.notes.repository.NoteRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    public void createNote(String topic, String content) {
        Note note = new Note();
        note.setCreatedAt(LocalDateTime.now());
        note.setTopic(topic);
        note.setContent(content);
        noteRepository.create(note);
    }

    public Note getNoteById(Long noteId) {
        return noteRepository.findById(noteId);
    }

    public List<Note> getAllNotes() {
        return noteRepository.findAll();
    }

    public void updateNote(Long noteId, String topic, String content) {
        Note note = noteRepository.findById(noteId);
        note.setTopic(topic);
        note.setContent(content);
        noteRepository.update(note);
    }

    public void deleteNote(Long noteId) {
        noteRepository.deleteById(noteId);
    }
}
