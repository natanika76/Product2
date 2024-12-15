package ru.natali.clinic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Schedule {
    private Long id;
    private Doctor doctor;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
}