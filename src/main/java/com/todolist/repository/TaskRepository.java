package com.todolist.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.todolist.model.Task;

public interface TaskRepository extends CrudRepository<Task, Long> {
    List<Task> findByUserId(Long userId);
}
