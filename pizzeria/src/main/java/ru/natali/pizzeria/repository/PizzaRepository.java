package ru.natali.pizzeria.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.natali.pizzeria.model.Pizza;

import java.util.List;
import java.util.Optional;

@Repository
public interface PizzaRepository extends JpaRepository<Pizza, Long> {

    @EntityGraph(attributePaths = {"ingredients", "ingredients.ingredient"})
    Optional<Pizza> findWithIngredientsById(Long id);

    List<Pizza> findByAvailableTrue();

    Optional<Pizza> findByName(String name);

    List<Pizza> findByPriceLessThan(Double price);

    @Modifying
    @Query("UPDATE Pizza p SET p.available = :available WHERE p.id = :id")
    void updateAvailability(@Param("id") Long id, @Param("available") Boolean available);

    @Query("SELECT p FROM Pizza p WHERE LOWER(p.name) LIKE LOWER(concat('%', :name,'%'))")
    List<Pizza> searchByNameContainingIgnoreCase(@Param("name") String name);

    @Override
    void flush();
}
