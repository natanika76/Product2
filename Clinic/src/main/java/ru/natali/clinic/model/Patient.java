package ru.natali.clinic.model;

import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Patient {
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;
    private String phoneNumber;
    private String email;
    private String address;

    public Patient(Long id) {
        this.id = id;
    }
}