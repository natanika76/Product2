package ru.natali.pizzeria.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.natali.pizzeria.dto.PizzaDTO;
import ru.natali.pizzeria.dto.PizzaIngredientDTO;
import ru.natali.pizzeria.exception.IngredientNotFoundException;
import ru.natali.pizzeria.exception.PizzaNotFoundException;
import ru.natali.pizzeria.mapper.PizzaIngredientMapper;
import ru.natali.pizzeria.mapper.PizzaMapper;
import ru.natali.pizzeria.model.Ingredient;
import ru.natali.pizzeria.model.Pizza;
import ru.natali.pizzeria.model.PizzaIngredient;
import ru.natali.pizzeria.model.PizzaIngredientId;
import ru.natali.pizzeria.repository.IngredientRepository;
import ru.natali.pizzeria.repository.PizzaIngredientRepository;
import ru.natali.pizzeria.repository.PizzaRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // Важно!
public class PizzaService {
    private final PizzaRepository pizzaRepository;
    private final PizzaMapper pizzaMapper;
    private final PizzaIngredientMapper pizzaIngredientMapper;
    private final IngredientRepository ingredientRepository;
    private final PizzaIngredientRepository
    pizzaIngredientRepository;

    public List<Pizza> getAvailablePizzas() {
        return pizzaRepository.findByAvailableTrue();
    }

    public List<PizzaDTO> findAll() {
        return pizzaRepository.findAll().stream()
                .map(pizzaMapper::toDto)
                .toList();
    }

     public PizzaDTO findById(Long id) {
         Pizza pizza = pizzaRepository.findWithIngredientsById(id)  // Этот метод должен загружать ингредиенты
                 .orElseThrow(() -> new PizzaNotFoundException(id));
         return pizzaMapper.toDto(pizza);
     }
    @Transactional
    public PizzaDTO create(PizzaDTO pizzaDTO) {

        Pizza pizza = pizzaMapper.toEntity(pizzaDTO);
        Pizza savedPizza = pizzaRepository.save(pizza);

        if (pizzaDTO.getIngredients() != null && !pizzaDTO.getIngredients().isEmpty()) {
            List<PizzaIngredient> pizzaIngredients = new ArrayList<>();

            for (PizzaIngredientDTO ingredientDTO : pizzaDTO.getIngredients()) {

                Ingredient ingredient = ingredientRepository.findById(ingredientDTO.getIngredientId())
                        .orElseThrow(() -> new IngredientNotFoundException(ingredientDTO.getIngredientId()));

                PizzaIngredient pizzaIngredient = new PizzaIngredient();
                pizzaIngredient.setPizza(savedPizza);
                pizzaIngredient.setIngredient(ingredient);
                pizzaIngredient.setQuantity(ingredientDTO.getQuantity());

                PizzaIngredientId id = new PizzaIngredientId();
                id.setPizzaId(savedPizza.getId());
                id.setIngredientId(ingredient.getId());
                pizzaIngredient.setId(id);

                pizzaIngredients.add(pizzaIngredient);
            }

            pizzaIngredientRepository.saveAll(pizzaIngredients);
        }

        return pizzaMapper.toDto(savedPizza);
    }
    @Transactional
    public PizzaDTO update(Long id, PizzaDTO pizzaDTO) {
        Pizza existingPizza = pizzaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pizza not found"));
        pizzaMapper.updatePizzaFromDto(pizzaDTO, existingPizza);
        Pizza updatedPizza = pizzaRepository.save(existingPizza);
        return pizzaMapper.toDto(updatedPizza);
    }

    @Transactional
    public void delete(Long id) {
        pizzaRepository.deleteById(id);
    }
}