package ru.natali.notes.app;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import ru.natali.notes.model.Note;
import ru.natali.notes.repository.NoteRepository;

import javax.sql.DataSource;
import java.time.LocalDateTime;

public class MainNote {
    public static void main(String[] args) {
        // Создаем DataSource
        DataSource dataSource = new DriverManagerDataSource(
                "jdbc:postgresql://localhost:5433/notebook",
                "postgres",
                "Lzmf2000"
        );

        // Создаем JdbcTemplate на основе DataSource
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

        // Создаем NoteRepository, передавая JdbcTemplate
        NoteRepository noteRepository = new NoteRepository(jdbcTemplate);

        // Тестирование CRUD-методов

        // 1. Создание новой заметки
        Note newNote = new Note();
        newNote.setCreatedAt(LocalDateTime.now());
        newNote.setTopic("Тестовая заметка");
        newNote.setContent("Это тестовая заметка для проверки CRUD-операций.");
        noteRepository.create(newNote);
        System.out.println("Заметка создана.");

        // 2. Получение всех заметок
        System.out.println("Все заметки:");
        noteRepository.findAll().forEach(System.out::println);

        // 3. Получение заметки по ID (note_id = 1)
        Long noteId = 17L;
        Note foundNote = noteRepository.findById(noteId);
        System.out.println("Найденная заметка по ID " + noteId + ": " + foundNote);

        // 4. Обновление заметки
        if (foundNote != null) {
            foundNote.setTopic("Обновленная тема");
            foundNote.setContent("Обновленный текст заметки.");
            noteRepository.update(foundNote);
            System.out.println("Заметка обновлена: " + foundNote);
        } else {
            System.out.println("Заметка с ID " + noteId + " не найдена.");
        }

        // 5. Удаление заметки
        if (foundNote != null) {
            noteRepository.deleteById(noteId);
            System.out.println("Заметка с ID " + noteId + " удалена.");
        } else {
            System.out.println("Заметка с ID " + noteId + " не найдена.");
        }

        // 6. Проверка, что заметка удалена
        System.out.println("Все заметки после удаления:");
        noteRepository.findAll().forEach(System.out::println);
    }
}