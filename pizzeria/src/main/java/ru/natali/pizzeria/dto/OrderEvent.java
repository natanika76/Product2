package ru.natali.pizzeria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderEvent {
    private Long orderId;
    private Long customerId;
    private String customerName;
    private String status;
    private ZonedDateTime eventTime;
    private Double totalPrice;
}
