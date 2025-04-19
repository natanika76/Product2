package ru.natali.taskmanager.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.natali.taskmanager.model.Task;
import ru.natali.taskmanager.repository.TaskRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tasketl3b")
public class TaskController {
    private final TaskRepository repository;
    public TaskController(TaskRepository repository){
        this.repository=repository;
    }

    @GetMapping
    public List<Task> getAllTask() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        Optional<Task> task=repository.findById(id);
        return task.map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Task> createTask(@RequestBody Task task){
        Task saved=repository.save(task);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Task> updateTask(@PathVariable Long id,@RequestBody Task task) {
        Optional<Task> existing= repository.findById(id);
        if(existing.isPresent()){
            Task updated= existing.get();

            updated.setEi(task.getEi());
            updated.setStatusTime(task.getStatusTime());
            updated.setDuration(task.getDuration());
            updated.setStatus(task.getStatus());
            updated.setGroupName(task.getGroupName());
            updated.setAssignment(task.getAssignment());
            repository.save(updated);
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        if(repository.existsById(id)){
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
