package ru.natali.pizzeria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.natali.pizzeria.model.Customer;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByActiveTrue();
    Optional<Customer> findByPhone(String phone);

    // Добавляем новые методы
    Optional<Customer> findByEmail(String email);
    List<Customer> findByNameContainingIgnoreCase(String name);
    List<Customer> findByRegistrationDateBetween(ZonedDateTime start, ZonedDateTime end);
    List<Customer> findAllByOrderByNameAsc();
    List<Customer> findTop5ByOrderByRegistrationDateDesc();

    @Query("SELECT c FROM Customer c WHERE c.active = true AND c.registrationDate > :date")
    List<Customer> findActiveCustomersRegisteredAfter(@Param("date") ZonedDateTime date);

}
