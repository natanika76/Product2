package ru.natali.pizzeria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDTO {
    private Long id;
    private Long orderId;
    private Long employeeId;
    private ZonedDateTime deliveryTime;
    private String status;
    private ZonedDateTime estimatedTime;
}