package com.todolist.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.todolist.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
