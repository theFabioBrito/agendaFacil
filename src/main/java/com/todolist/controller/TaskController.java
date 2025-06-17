package com.todolist.controller;

import com.todolist.model.Task;
import com.todolist.model.User;
import com.todolist.repository.TaskRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
public class TaskController {

    private final TaskRepository taskRepository;

    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("tasks", taskRepository.findByUserId(user.getId()));
        model.addAttribute("task", new Task());
        return "index";
    }

    @PostMapping("/tasks")
    public String addTask(@ModelAttribute Task task, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        task.setUserId(user.getId());
        taskRepository.save(task);
        return "redirect:/";
    }

    @PostMapping("/tasks/delete/{id}")
    public String deleteTask(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Task task = taskRepository.findById(id).orElse(null);
        if (task != null && task.getUserId().equals(user.getId())) {
            taskRepository.deleteById(id);
        }

        return "redirect:/";
    }
}
