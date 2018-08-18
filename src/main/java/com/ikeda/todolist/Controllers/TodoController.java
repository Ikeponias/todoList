package com.ikeda.todolist.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ikeda.todolist.Models.Todo;
import com.ikeda.todolist.Services.TodoService;

@Controller
@EnableAutoConfiguration
@RequestMapping("/todos")
public class TodoController {
	@Autowired
	TodoService todoService;

	@RequestMapping(method = RequestMethod.GET)
	public String index(Model model) {
		List<Todo> todos = todoService.findAll();
		model.addAttribute("todos", todos);
		model.addAttribute("link_to_show", "/todos/show");
		model.addAttribute("link_to_new", "/todos/new");
		model.addAttribute("link_to_update", "/todos/update");
		model.addAttribute("link_to_destroy", "/todos/destroy");

		return "todos/index";
	}

	@RequestMapping(value = "/show/{id}", method = RequestMethod.GET)
	public String show(Model model, @PathVariable("id") Long id) {
		Todo todo = todoService.find(id).get();
		model.addAttribute("todo", todo);
		model.addAttribute("link_to_child_new", "/todos/new");

		return "todos/show";
	}

	@RequestMapping(value = { "/new", "/new/{parent_id}" }, method = RequestMethod.GET)
	public String newTodo(Model model, @PathVariable(name = "parent_id", required = false) Optional<Long> parent_id) {
		Todo todo = new Todo();

		model.addAttribute("todoForm", todo);
		if (parent_id.isPresent()) {
			model.addAttribute("link_to_create", "/todos/create" + "/" + parent_id.get().toString());
		} else {
			model.addAttribute("link_to_create", "/todos/create");
		}

		return "todos/new";
	}

	@RequestMapping(value = { "/create", "/create/{parent_id}" }, method = RequestMethod.POST)
	public String create(@ModelAttribute Todo todoForm,
			@PathVariable(name = "parent_id", required = false) Optional<Long> parent_id) {

		if (parent_id.isPresent()) {
			Todo parentTodo = todoService.find(parent_id.get()).get();
			todoForm.setParent(parentTodo);
		}

		todoService.save(todoForm);

		return "redirect:/todos";
	}

	@RequestMapping(value = "/update/{id}", method = RequestMethod.PUT)
	public String update(@PathVariable("id") Long id) {
		Todo todo = todoService.find(id).get();
		todo.switchDone();
		todoService.save(todo);

		return "redirect:/todos";
	}

	@RequestMapping(value = "/destroy/{id}", method = RequestMethod.DELETE)
	public String destroy(@PathVariable("id") Long id) {
		List<Todo> childrenTodo = todoService.find(id).get().getChildren();
		if (childrenTodo != null) {
			childrenTodo.forEach(child -> todoService.delete(child.getId()));
		}
		todoService.delete(id);

		return "redirect:/todos";
	}
}