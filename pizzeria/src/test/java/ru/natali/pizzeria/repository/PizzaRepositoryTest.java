package ru.natali.pizzeria.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;
import ru.natali.pizzeria.model.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PizzaRepositoryTest {

    @Autowired
    private PizzaRepository pizzaRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    @Autowired
    private PizzaIngredientRepository pizzaIngredientRepository;

    @Autowired
    private EntityManager entityManager;

    private Pizza pizza;

    @BeforeEach
    void setUp() {

        pizzaIngredientRepository.deleteAll();
        orderRepository.deleteAll();
        pizzaRepository.deleteAll();
        ingredientRepository.deleteAll();
        customerRepository.deleteAll();

        Ingredient ingredient1 = new Ingredient();
        ingredient1.setName("Tomato");
        ingredient1.setStockQuantity(10.0);
        ingredient1.setUnit("kg");
        ingredient1.setCostPerUnit(5.0);

        Ingredient ingredient2 = new Ingredient();
        ingredient2.setName("Cheese");
        ingredient2.setStockQuantity(5.0);
        ingredient2.setUnit("kg");
        ingredient2.setCostPerUnit(10.0);

        ingredientRepository.saveAll(List.of(ingredient1, ingredient2));

        pizza = new Pizza();
        pizza.setName("Margherita");
        pizza.setDescription("Classic pizza");
        pizza.setPrice(10.0);
        pizza.setAvailable(true);
        pizza = pizzaRepository.save(pizza);

        PizzaIngredientId id1 = new PizzaIngredientId(pizza.getId(), ingredient1.getId());
        PizzaIngredient pi1 = new PizzaIngredient();
        pi1.setId(id1);
        pi1.setPizza(pizza);
        pi1.setIngredient(ingredient1);
        pi1.setQuantity(0.2);

        PizzaIngredientId id2 = new PizzaIngredientId(pizza.getId(), ingredient2.getId());
        PizzaIngredient pi2 = new PizzaIngredient();
        pi2.setId(id2);
        pi2.setPizza(pizza);
        pi2.setIngredient(ingredient2);
        pi2.setQuantity(0.15);

        pizzaIngredientRepository.saveAll(List.of(pi1, pi2));
    }

    @Test
    void findByAvailableTrue_ShouldReturnOnlyAvailablePizzas() {
        Pizza unavailablePizza = new Pizza();
        unavailablePizza.setName("Unavailable");
        unavailablePizza.setPrice(15.0);
        unavailablePizza.setAvailable(false);
        pizzaRepository.save(unavailablePizza);

        List<Pizza> result = pizzaRepository.findByAvailableTrue();

        assertEquals(1, result.size());
        assertTrue(result.get(0).getAvailable());
    }

    @Test
    void findByName_ShouldReturnPizza() {
        Optional<Pizza> result = pizzaRepository.findByName("Margherita");

        assertTrue(result.isPresent());
        assertEquals("Margherita", result.get().getName());
    }

    @Test
    void findByPriceLessThan_ShouldReturnFilteredPizzas() {
        Pizza expensivePizza = new Pizza();
        expensivePizza.setName("Expensive");
        expensivePizza.setPrice(20.0);
        expensivePizza.setAvailable(true);
        pizzaRepository.save(expensivePizza);

        List<Pizza> result = pizzaRepository.findByPriceLessThan(15.0);

        assertEquals(1, result.size());
        assertEquals("Margherita", result.get(0).getName());
    }

    @Test
    @Transactional
    void updateAvailability_ShouldChangeStatus() {

        Pizza initialPizza = pizzaRepository.findById(pizza.getId()).orElseThrow();
        assertTrue(initialPizza.getAvailable());

        pizzaRepository.updateAvailability(pizza.getId(), false);

        entityManager.flush();
        entityManager.clear();

        Pizza updatedPizza = pizzaRepository.findById(pizza.getId()).orElseThrow();
        assertFalse(updatedPizza.getAvailable()); // Теперь должно пройти
    }

    @Test
    void searchByNameContainingIgnoreCase_ShouldFindPizzas() {
        List<Pizza> result = pizzaRepository.searchByNameContainingIgnoreCase("marg");

        assertEquals(1, result.size());
        assertEquals("Margherita", result.get(0).getName());
    }

}
