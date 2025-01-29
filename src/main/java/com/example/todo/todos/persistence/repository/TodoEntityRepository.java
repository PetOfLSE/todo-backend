package com.example.todo.todos.persistence.repository;

import com.example.todo.todos.persistence.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoEntityRepository extends JpaRepository<TodoEntity, Long> {
}
