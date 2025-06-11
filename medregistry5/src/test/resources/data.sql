-- Delete existing test data
DELETE FROM doctor;

-- Insert test data
INSERT INTO doctor (id, first_name, last_name, specialization, deleted) VALUES
                                                                            (1, 'Иван', 'Потапов', 'кардиолог', false),
                                                                            (2, 'Эмилия', 'Жданова', 'нейрохирург', false);