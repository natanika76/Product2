package ru.natali.pizzeria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemEvent {
    private Long pizzaId;
    private String pizzaName;
    private Integer quantity;
    private Double price;
}
