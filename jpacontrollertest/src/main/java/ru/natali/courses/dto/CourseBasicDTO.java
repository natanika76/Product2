package ru.natali.courses.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.natali.courses.model.Course;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CourseBasicDTO {
    private Long id;
    private String name;
    private LocalDate startDate;
    private boolean active;

    public CourseBasicDTO(Course course) {
        this.id = course.getId();
        this.name = course.getName();
        this.startDate = course.getStartDate();
        this.active = course.isActive();
    }
}
