package ru.natali.pizzeria.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.natali.pizzeria.dto.PizzaDTO;
import ru.natali.pizzeria.model.Pizza;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PizzaMapperTest {

    @Autowired
    private PizzaMapper pizzaMapper;

    @Test
    void testToDto() {
        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setName("Margherita");
        pizza.setDescription("Classic pizza");
        pizza.setPrice(12.99);
        pizza.setAvailable(true);
        pizza.setCookingTimeMin(15);
        pizza.setCategory("CLASSIC");

        PizzaDTO dto = pizzaMapper.toDto(pizza);

        assertEquals(pizza.getId(), dto.getId());
        assertEquals(pizza.getName(), dto.getName());
        assertEquals(pizza.getDescription(), dto.getDescription());
        assertEquals(pizza.getPrice(), dto.getPrice());
        assertEquals(pizza.getAvailable(), dto.getAvailable());
        assertEquals(pizza.getCookingTimeMin(), dto.getCookingTimeMin());
        assertEquals(pizza.getCategory(), dto.getCategory());
    }

    @Test
    void testToEntity() {
        PizzaDTO dto = new PizzaDTO();
        dto.setId(1L);
        dto.setName("Margherita");
        dto.setDescription("Classic pizza");
        dto.setPrice(12.99);
        dto.setAvailable(true);
        dto.setCookingTimeMin(15);
        dto.setCategory("CLASSIC");

        Pizza pizza = pizzaMapper.toEntity(dto);

        assertEquals(dto.getId(), pizza.getId());
        assertEquals(dto.getName(), pizza.getName());
        assertEquals(dto.getDescription(), pizza.getDescription());
        assertEquals(dto.getPrice(), pizza.getPrice());
        assertEquals(dto.getAvailable(), pizza.getAvailable());
        assertEquals(dto.getCookingTimeMin(), pizza.getCookingTimeMin());
        assertEquals(dto.getCategory(), pizza.getCategory());
        assertNull(pizza.getIngredients());
    }

    @Test
    void testUpdatePizzaFromDto() {
        Pizza pizza = new Pizza();
        pizza.setId(1L);
        pizza.setName("Old Name");
        pizza.setAvailable(false);

        PizzaDTO dto = new PizzaDTO();
        dto.setName("New Name");
        dto.setDescription("Updated description");
        dto.setAvailable(true);

        pizzaMapper.updatePizzaFromDto(dto, pizza);

        assertEquals(1L, pizza.getId());
        assertEquals("New Name", pizza.getName());
        assertEquals("Updated description", pizza.getDescription());
        assertTrue(pizza.getAvailable());
        assertNull(pizza.getPrice());
    }
}
