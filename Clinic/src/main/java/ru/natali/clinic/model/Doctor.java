package ru.natali.clinic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {
    private Long id;
    private String firstName;
    private String lastName;
    private String specialty;
    private String phoneNumber;
    private String email;
    private String officeNumber;

    public Doctor(Long id) {
        this.id = id;
    }

    public Doctor(Long id, String expectedFirstName, String expectedLastName, String expectedSpecialty, String expectedPhoneNumber) {
        this.id = id;
        this.firstName = expectedFirstName;
        this.lastName = expectedLastName;
        this.specialty = expectedSpecialty;
        this.phoneNumber = expectedPhoneNumber;
    }
}
