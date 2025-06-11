package ru.natali.medregistry.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "patient")
@SQLDelete(sql = "UPDATE patient SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "insurance_number", unique = true)
    private String insuranceNumber;

    @Column(nullable = false)
    private boolean deleted = false;
}
