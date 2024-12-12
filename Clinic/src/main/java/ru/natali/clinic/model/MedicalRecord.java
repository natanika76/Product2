package ru.natali.clinic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecord {
    private Long id;
    private Patient patient;
    private Doctor doctor;
    private String diagnosis;
    private String treatment;
    private LocalDateTime recordDate;
}
