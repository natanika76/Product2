package ru.natali.clinic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDetails {
    private Long id;
    private String patientFirstName;
    private String patientLastName;
    private String doctorFirstName;
    private String doctorLastName;
    private LocalDateTime appointmentDate;
    private String status;

    @Override
    public String toString() {
        return "AppointmentDetails{" +
                "id=" + id +
                ", patientFirstName='" + patientFirstName + '\'' +
                ", patientLastName='" + patientLastName + '\'' +
                ", doctorFirstName='" + doctorFirstName + '\'' +
                ", doctorLastName='" + doctorLastName + '\'' +
                ", appointmentDate=" + appointmentDate.format(DateTimeFormatter.ofPattern("d:MMM:uuuu HH:mm")) +
                ", status='" + status + '\'' +
                '}';
    }
}
