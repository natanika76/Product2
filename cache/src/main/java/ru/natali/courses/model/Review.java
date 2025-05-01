package ru.natali.courses.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "reviews", schema = "courses1_schema")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
/*
Для предотвращения конфликтов сериализации мы используем аннотацию @JsonBackReference,
чтобы Джексон понимал, что эти объекты принадлежат другому направлению отношений.
 */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    @JsonBackReference(value = "review-student") // Обратный путь для студента
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @JsonBackReference(value = "course-reviews") // Обратный путь для курса
    private Course course;

    @Column(length = 1024, nullable = false)
    private String reviewText;

    @Min(1)
    @Max(5)
    @Column(nullable = false)
    private int rating;

    @Column(updatable = false)
    private LocalDateTime createdAt;
}