package ru.natali.courses.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "courses", schema = "courses_schema")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date startDate;

    @Column(nullable = false)
    private boolean active;

    public Course() {
    }

    public Course(Long id, String name, Date startDate, boolean active) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.active = active;
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
}
