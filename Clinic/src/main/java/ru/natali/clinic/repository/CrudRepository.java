package ru.natali.clinic.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T> {
    Optional<T> find(Long id);
    void save(T model);
    void update(T model);
    void delete(Long id);
    List<T> findAll();
    void deleteAll(); // Добавленный метод - его не было
}
