package ru.natali.courses.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Упрощенное DTO для представления студента.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SimpleStudentDTO {
    private Long id;
    private String firstName;
    private String lastName;
}