package ru.natali.courses.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.util.HashSet;
import java.util.Set;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "students", schema = "courses1_schema")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @JsonBackReference(value = "course-student") // Обратное направление
    private Course course;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference(value = "review-student") // Обратное направление
    private List<Review> reviews;

    @ManyToMany
    @JoinTable(
            name = "student_courses",
            schema = "courses1_schema",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    @LazyCollection(LazyCollectionOption.EXTRA) // ЛЕНЬВАЯ ЗАГРУЗКА!
    private Set<Course> enrolledCourses = new HashSet<>();

    // Новые поля с соответствующими аннотациями
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    // Правильное определение Enum-филда
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition="VARCHAR(32)")
    private Role role;
}
