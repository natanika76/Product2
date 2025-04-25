-- liquibase formatted sql

-- changeset natali:9
INSERT INTO courses1_schema.reviews (student_id, course_id, review_text, rating, created_at)
VALUES
    (1, 1, 'Отличный курс! Все понятно объясняли.', 5, NOW()),
    (2, 1, 'Курс хороший, но хотелось бы больше практики.', 4, NOW()),
    (3, 2, 'Очень интересный материал, рекомендую!', 5, NOW()),
    (4, 2, 'Курс отличный, но иногда было сложно понимать.', 4, NOW());