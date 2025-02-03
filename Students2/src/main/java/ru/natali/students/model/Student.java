package ru.natali.students.model;

import lombok.*;

import java.util.List;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class Student {
    private Long id;
    private String fullName;
    private String email;
    private List<String> courses;

    public Student(Long id, String fullName, String email, List<String> courses) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.courses = new ArrayList<>(courses); // Создаём изменяемый список
    }
}