package ru.natali.courses.model;

import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

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
    private List<Review> reviews;

    @Column(name = "is_archived", nullable = false)
    private boolean archived;

    public Course() {
    }

    public Course(Long id, String name, Date startDate, boolean active) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.active = active;
    }

    public Course(Long id, String name, Date startDate, boolean active, List<Review> reviews, boolean archived) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.active = active;
        this.reviews = reviews;
        this.archived = archived;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
}
