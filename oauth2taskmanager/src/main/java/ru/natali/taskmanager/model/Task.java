package ru.natali.taskmanager.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasketl3b",catalog = "spark")
public class Task{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="ЕИ")
    private Double ei;

    @Column(name = "StatusTime")
    private LocalDateTime statusTime;

    @Column(name = "Длительность",nullable = false)
    private Double duration=0.0;

    @Column(name = "Статус",columnDefinition = "LONGTEXT")
    private String status;

    @Column(name = "Группа",columnDefinition = "LONGTEXT")
    private String groupName;

    @Column(name = "Назначение")
    private Integer assignment=0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getEi() {
        return ei;
    }

    public void setEi(Double ei) {
        this.ei = ei;
    }

    public LocalDateTime getStatusTime() {
        return statusTime;
    }

    public void setStatusTime(LocalDateTime statusTime) {
        this.statusTime = statusTime;
    }

    public Double getDuration() {
        return duration;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getAssignment() {
        return assignment;
    }

    public void setAssignment(Integer assignment) {
        this.assignment = assignment;
    }
}

