package ru.natali.courses.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "courses", schema = "courses1_schema")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "start_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Column(name = "is_active", nullable = false)
    private boolean active;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "course-reviews") // Управляемое направление
    private List<Review> reviews;

    @Column(name = "is_archived", nullable = false)
    private boolean archived;

    @ManyToMany(mappedBy = "enrolledCourses")
    @JsonManagedReference(value = "course-student") // Управляемое направление
    private Set<Student> students = new HashSet<>();
}
