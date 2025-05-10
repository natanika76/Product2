package ru.natali.clinic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medservice {
    private Long id;
    private String name;
    private String description;
    private double price;

    public Medservice(Long id) {
        this.id = id;
    }
}
