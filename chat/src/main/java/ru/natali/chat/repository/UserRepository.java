package ru.natali.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.natali.chat.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}