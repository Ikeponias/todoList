package com.ikeda.todolist.Repositories;

import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import com.ikeda.todolist.Models.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
	@Lock(LockModeType.OPTIMISTIC)
	Optional<Todo> findById(Long id);
}