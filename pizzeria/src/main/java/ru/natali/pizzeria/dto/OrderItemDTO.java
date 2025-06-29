package ru.natali.pizzeria.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private Long id;

    @NotNull
    private Long pizzaId; // вместо Pizza

    @Min(1)
    private Integer quantity;

    @Positive
    private Double price;

    private String specialRequests;
}




