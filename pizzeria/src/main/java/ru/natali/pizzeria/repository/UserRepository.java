package ru.natali.pizzeria.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.natali.pizzeria.model.User;


import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    @Modifying
    @Query("UPDATE User u SET u.deleted = true WHERE u.id = ?1")
    void softDelete(Long id);

    @Query("SELECT u FROM User u WHERE u.deleted = false")
    List<User> findAllActive();
}