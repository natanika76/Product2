package ru.natali.students.model;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    private Long id;
    private String fullName;
    private String email;
    private List<String> courses;
}