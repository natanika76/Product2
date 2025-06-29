package ru.natali.pizzeria.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.natali.pizzeria.dto.PizzaIngredientDTO;
import ru.natali.pizzeria.model.Ingredient;
import ru.natali.pizzeria.model.PizzaIngredient;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PizzaIngredientMapperTest {

    @Autowired
    private PizzaIngredientMapper pizzaIngredientMapper;

    @Test
    void testToDto() {
        PizzaIngredient pizzaIngredient = new PizzaIngredient();
        pizzaIngredient.setQuantity(0.5);

        Ingredient ingredient = new Ingredient();
        ingredient.setId(3L);
        pizzaIngredient.setIngredient(ingredient);

        PizzaIngredientDTO dto = pizzaIngredientMapper.toDto(pizzaIngredient);

        assertEquals(pizzaIngredient.getIngredient().getId(), dto.getIngredientId());
        assertEquals(pizzaIngredient.getQuantity(), dto.getQuantity());
    }

    @Test
    void testToEntity() {
        PizzaIngredientDTO dto = new PizzaIngredientDTO();
        dto.setIngredientId(3L);
        dto.setQuantity(0.5);

        PizzaIngredient pizzaIngredient = pizzaIngredientMapper.toEntity(dto);

        assertNotNull(pizzaIngredient.getId());
        assertNull(pizzaIngredient.getId().getPizzaId());
        assertNull(pizzaIngredient.getId().getIngredientId());

        assertEquals(dto.getQuantity(), pizzaIngredient.getQuantity());
        assertNull(pizzaIngredient.getPizza());
        assertNull(pizzaIngredient.getIngredient());
    }

    @Test
    void testCompositeIdStructure() {
        PizzaIngredientDTO dto = new PizzaIngredientDTO();
        dto.setIngredientId(3L);
        dto.setQuantity(1.0);

        PizzaIngredient entity = pizzaIngredientMapper.toEntity(dto);

        assertNotNull(entity.getId());
        assertNull(entity.getId().getPizzaId());
        assertNull(entity.getId().getIngredientId());

        assertEquals(1.0, entity.getQuantity());
    }

    @Test
    void testToEntityWithNullDto() {
        PizzaIngredient entity = pizzaIngredientMapper.toEntity(null);
        assertNull(entity);
    }

    @Test
    void testToDtoWithNullEntity() {
        PizzaIngredientDTO dto = pizzaIngredientMapper.toDto(null);
        assertNull(dto);
    }
}
