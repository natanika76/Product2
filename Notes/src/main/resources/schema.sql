CREATE TABLE IF NOT EXISTS notes (
                       note_id BIGSERIAL PRIMARY KEY,  -- Уникальный идентификатор заметки
                       created_at TIMESTAMP(0) NOT NULL DEFAULT NOW(),  -- Дата и время создания заметки (без миллисекунд)
                       topic VARCHAR(255) NOT NULL,  -- Тема заметки
                       content TEXT NOT NULL  -- Текст заметки
);

COMMENT ON TABLE notes IS 'Таблица для хранения пользовательских текстовых заметок.';

COMMENT ON COLUMN notes.note_id IS 'Уникальный идентификатор заметки. Автоматически генерируется.';
COMMENT ON COLUMN notes.created_at IS 'Дата и время создания заметки (без миллисекунд). По умолчанию устанавливается текущее время.';
COMMENT ON COLUMN notes.topic IS 'Тема заметки. Ограничение длины 255 символов.';
COMMENT ON COLUMN notes.content IS 'Текст заметки. Может содержать неограниченное количество символов.';
