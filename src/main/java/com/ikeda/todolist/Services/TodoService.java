package com.ikeda.todolist.Services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikeda.todolist.Models.Todo;
import com.ikeda.todolist.Repositories.TodoRepository;

@Service
public class TodoService {
	@Autowired
	TodoRepository todoRepository;

	public List<Todo> findAll() {
		return todoRepository.findAll();
	}

	public Optional<Todo> find(Long id) {
		return todoRepository.findById(id);
	}

	public Todo save(Todo todo) {
		return todoRepository.save(todo);
	}

	public void delete(Long id) {
		todoRepository.deleteById(id);
	}
}
