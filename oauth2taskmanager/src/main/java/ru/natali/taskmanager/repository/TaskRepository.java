package ru.natali.taskmanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.natali.taskmanager.model.Task;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {}
