package ru.natali.pizzeria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.natali.pizzeria.model.PizzaIngredient;
import ru.natali.pizzeria.model.PizzaIngredientId;

@Repository
public interface PizzaIngredientRepository extends JpaRepository<PizzaIngredient, PizzaIngredientId> {

    long countByPizzaId(Long pizzaId);

    @Query("SELECT COUNT(pi) FROM PizzaIngredient pi WHERE pi.pizza.id = :pizzaId")
    long countByPizzaIdCustom(@Param("pizzaId") Long pizzaId);
}
