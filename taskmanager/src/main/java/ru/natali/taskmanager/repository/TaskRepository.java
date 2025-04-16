package ru.natali.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.natali.taskmanager.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long> {}