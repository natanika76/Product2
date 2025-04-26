package ru.natali.courses.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SimpleCourseDTO {

    private Long id;
    private String name;
    private LocalDate startDate;
    private boolean isActive;
}