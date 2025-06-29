package ru.natali.pizzeria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {
    private Long id;
    private String name;
    private String position;
    private String phone;
    private String email;
    private LocalDate hireDate;
    private Boolean active;
}

