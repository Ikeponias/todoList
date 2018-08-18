package com.ikeda.todolist.Controllers;

import java.util.List;

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
		model.addAttribute("link_to_new", "/todos/new");

		return "todos/show";
	}

	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String newForm(Model model) {
		model.addAttribute("todoForm", new Todo());

		return "todos/new";
	}

	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public String create(@ModelAttribute Todo todoForm) {
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
		todoService.delete(id);
		return "redirect:/todos";
	}
}