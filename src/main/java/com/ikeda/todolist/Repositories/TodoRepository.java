package com.ikeda.todolist.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ikeda.todolist.Models.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {
}