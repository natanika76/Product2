package ru.natali.earthquake.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDateTime;

@Entity
public class Earthquake {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private LocalDateTime time;
    private double magnitude;
    private String place;

    public Earthquake() {
    }

    public Earthquake(Long id, String title, LocalDateTime time, double magnitude, String place) {
        this.id = id;
        this.title = title;
        this.time = time;
        this.magnitude = magnitude;
        this.place = place;
    }

    public Earthquake(String title, LocalDateTime time, double magnitude, String place) {
        this.title = title;
        this.time = time;
        this.magnitude = magnitude;
        this.place = place;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(double magnitude) {
        this.magnitude = magnitude;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
