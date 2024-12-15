package ru.natali.clinic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    @Override
    public String toString() {
        return "MedicalRecord{" +
                "id=" + id +
                ", patient=" + patient +
                ", doctor=" + doctor +
                ", diagnosis='" + diagnosis + '\'' +
                ", treatment='" + treatment + '\'' +
                ", recordDate=" + recordDate.format(DateTimeFormatter.ofPattern("d:MMM:uuuu HH:mm")) +
                '}';
    }
}
