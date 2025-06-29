package ru.natali.pizzeria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.natali.pizzeria.model.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}