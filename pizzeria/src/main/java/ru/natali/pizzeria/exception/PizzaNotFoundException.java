package ru.natali.pizzeria.exception;

public class PizzaNotFoundException extends RuntimeException {
    public PizzaNotFoundException(Long id) {
        super("Pizza not found with id: " + id);
    }
}
