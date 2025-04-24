package ru.natali.courses.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Упрощенное DTO для представления курса.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SimpleCourseDTO {
    private Long id;
    private String name;
    private String startDate;
    private boolean isActive;
}
