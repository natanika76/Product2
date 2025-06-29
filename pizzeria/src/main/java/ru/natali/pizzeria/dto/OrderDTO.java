package ru.natali.pizzeria.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private Long id;

    @NotNull(message = "Customer ID is required")
    private Long customerId;

    private ZonedDateTime orderDate;

    private String status;

    @NotNull(message = "Total price is required")
    @Positive(message = "Total price must be positive")
    private Double totalPrice;

    @NotBlank(message = "Delivery address is required")
    private String deliveryAddress;

    private String deliveryNotes;

    private String paymentMethod;

    private String paymentStatus;

    @NotEmpty
    private List<@Valid OrderItemDTO> items;
}




