package ru.natali.pizzeria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.natali.pizzeria.model.Order;

import java.time.ZonedDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(String status);
    List<Order> findByCustomerId(Long customerId);
    List<Order> findByOrderDateBetween(ZonedDateTime start, ZonedDateTime end);
}