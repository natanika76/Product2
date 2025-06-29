package ru.natali.pizzeria.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Entity
@Table(name = "deliveries")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Column(name = "delivery_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime deliveryTime;

    @Column(nullable = false)
    private String status = "PENDING";

    @Column(name = "estimated_time", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    private ZonedDateTime estimatedTime;
}
