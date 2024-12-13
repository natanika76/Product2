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
public class Appointment {
    private Long id;
    private Patient patient;
    private Doctor doctor;
    private Medservice service;
    private LocalDateTime appointmentDate;
    private String status;
    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", patient=" + patient +
                ", doctor=" + doctor +
                ", service=" + service +
                ", appointmentDate=" + appointmentDate.format(DateTimeFormatter.ofPattern("d:MMM:uuuu HH:mm")) +
                ", status='" + status + '\'' +
                '}';
    }
}
