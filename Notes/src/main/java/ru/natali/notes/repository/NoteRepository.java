package ru.natali.notes.repository;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.natali.notes.model.Note;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class NoteRepository {

    private final JdbcTemplate jdbcTemplate;

    public NoteRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void create(Note note) {
        String sql = "INSERT INTO notes (created_at, topic, content) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, note.getCreatedAt(), note.getTopic(), note.getContent());
    }

    public List<Note> findAll() {
        String sql = "SELECT * FROM notes";
        return jdbcTemplate.query(sql, new NoteRowMapper());
    }

    public Note findById(Long noteId) {
        String sql = "SELECT * FROM notes WHERE note_id = ?";
        return jdbcTemplate.queryForObject(sql, new NoteRowMapper(), noteId);
    }

    public void update(Note note) {
        String sql = "UPDATE notes SET created_at = ?, topic = ?, content = ? WHERE note_id = ?";
        jdbcTemplate.update(sql, note.getCreatedAt(), note.getTopic(), note.getContent(), note.getNoteId());
    }

    public void deleteById(Long noteId) {
        String sql = "DELETE FROM notes WHERE note_id = ?";
        jdbcTemplate.update(sql, noteId);
    }

    public void deleteAll() {
        String sql = "DELETE FROM notes";
        jdbcTemplate.update(sql);
    }

    private static final class NoteRowMapper implements RowMapper<Note> {
        @Override
        public Note mapRow(ResultSet rs, int rowNum) throws SQLException {
            Note note = new Note();
            note.setNoteId(rs.getLong("note_id"));
            note.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
            note.setTopic(rs.getString("topic"));
            note.setContent(rs.getString("content"));
            return note;
        }
    }
}