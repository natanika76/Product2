package ru.natali.taskmanager.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.natali.taskmanager.model.Task;
import ru.natali.taskmanager.repository.TaskRepository;

import java.util.List;

@Controller
public class TaskWebController {

    @Autowired
    private TaskRepository taskRepository;

    @GetMapping("/tasks")
    public String showTasks(Model model) {
        List<Task> tasks = taskRepository.findAll(); // Получаем все задачи
        model.addAttribute("tasks", tasks); // Передаем задачи в представление
        return "tasks"; // Рендерим страницу
    }

    // Добавляем новую задачу
    // POST /add-task — только USER и ADMIN
    @PostMapping("/add-task")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")  // VIEWER не может!
    public String addTask(@RequestParam String name, @RequestParam String description) {
        Task task = new Task();
        task.setName(name);
        task.setDescription(description);
        taskRepository.save(task);
        return "redirect:/tasks";
    }

    // POST /delete/{id} — только ADMIN
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")  // Только ADMIN!
    public String deleteTask(@PathVariable Long id) {
        taskRepository.deleteById(id);
        return "redirect:/tasks";
    }
    //http://localhost:8080/api/tasks  выдаст JSON, а не HTML
    @GetMapping(value = "/api/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<Task> getTasksAsJson() {
        return taskRepository.findAll();
    }
}