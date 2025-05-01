package ru.natali.courses.dto;

import lombok.Data;
import ru.natali.courses.model.Course;
import ru.natali.courses.model.Student;

import java.util.List;
import java.util.stream.Collectors;
/*
@Data
public class EmptyAssignmentReportDTO {
    private List<Long> studentsWithoutCoursesIds;
    private List<Long> coursesWithoutStudentsIds;

    public EmptyAssignmentReportDTO(List<Student> students, List<Course> courses) {
        this.studentsWithoutCoursesIds = students.stream()
                .map(Student::getId)
                .collect(Collectors.toList());

        this.coursesWithoutStudentsIds = courses.stream()
                .map(Course::getId)
                .collect(Collectors.toList());
    }
}*/
@Data
public class EmptyAssignmentReportDTO {
    private final List<Student> studentsWithoutCourses;
    private final List<Course> coursesWithoutStudents;

    public EmptyAssignmentReportDTO(List<Student> studentsWithoutCourses, List<Course> coursesWithoutStudents) {
        this.studentsWithoutCourses = studentsWithoutCourses;
        this.coursesWithoutStudents = coursesWithoutStudents;
    }

    // Геттеры нужны для извлечения данных
    public List<Student> getStudentsWithoutCourses() {
        return studentsWithoutCourses;
    }

    public List<Course> getCoursesWithoutStudents() {
        return coursesWithoutStudents;
    }
}