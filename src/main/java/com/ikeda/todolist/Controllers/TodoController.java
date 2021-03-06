package com.ikeda.todolist.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ikeda.todolist.Constants.RoutingURL;
import com.ikeda.todolist.Models.Todo;
import com.ikeda.todolist.Services.TodoService;

@Controller
@EnableAutoConfiguration
@RequestMapping(RoutingURL.TODOS)
public class TodoController {
	@Autowired
	TodoService todoService;

	@RequestMapping(value = RoutingURL.INDEX, method = RequestMethod.GET)
	public String index(Model model) {
		List<Todo> todos = todoService.findAll();
		model.addAttribute("todos", todos);
		model.addAttribute("link_to_edit", RoutingURL.TODOS_EDIT);
		model.addAttribute("link_to_new", RoutingURL.TODOS_NEW);
		model.addAttribute("link_to_update", RoutingURL.TODOS_OPTIMISM_UPDATE);
		model.addAttribute("link_to_destroy", RoutingURL.TODOS_DESTROY);
		model.addAttribute("link_to_place", RoutingURL.PLACES_INDEX);

		return "todos/index";
	}

	@RequestMapping(value = { RoutingURL.NEW, RoutingURL.NEW + "/{parent_id}" }, method = RequestMethod.GET)
	public String newTodo(Model model, @PathVariable(name = "parent_id", required = false) Optional<Long> parent_id) {
		Todo todo = new Todo();

		model.addAttribute("todoForm", todo);
		if (parent_id.isPresent()) {
			model.addAttribute("link_to_create", RoutingURL.TODOS_CREATE + "/" + parent_id.get().toString());
		} else {
			model.addAttribute("link_to_create", RoutingURL.TODOS_CREATE);
		}

		return "todos/new";
	}

	@RequestMapping(value = { RoutingURL.CREATE, RoutingURL.CREATE + "/{parent_id}" }, method = RequestMethod.POST)
	public String create(@ModelAttribute Todo todoForm,
			@PathVariable(name = "parent_id", required = false) Optional<Long> parent_id) {

		if (parent_id.isPresent()) {
			Todo parentTodo = todoService.find(parent_id.get()).get();
			todoForm.setParent(parentTodo);
		}

		todoService.save(todoForm);

		return "redirect:" + RoutingURL.TODOS_INDEX;
	}


	@RequestMapping(value = RoutingURL.EDIT + "/{id}", method = RequestMethod.GET)
	public String edit(Model model, @PathVariable("id") Long id) {
		Todo todo = todoService.find(id).get();
		
		model.addAttribute("todoForm", todo);
		model.addAttribute("children_todo", todo.getChildren());
		model.addAttribute("link_to_update", RoutingURL.TODOS_OPTIMISM_UPDATE);
		model.addAttribute("link_to_child_new", RoutingURL.TODOS_NEW);

		return "todos/edit";
	}
	
	@RequestMapping(value = RoutingURL.OPTIMISM_UPDATE + "/{id}", method = RequestMethod.PUT)
	public String optimismUpdate(@ModelAttribute Todo todoForm, @PathVariable("id") Long id) {
		Todo todo = todoService.find(id).get();

		if(todo.getVersion() != todoForm.getVersion()){
            throw new ObjectOptimisticLockingFailureException(Todo.class, todoForm.getId());
        }

		todoService.save(todoForm);

		return "redirect:" + RoutingURL.TODOS_INDEX;
	}

	@RequestMapping(value = RoutingURL.DESTROY + "/{id}", method = RequestMethod.DELETE)
	public String destroy(@PathVariable("id") Long id) {
		List<Todo> childrenTodo = todoService.find(id).get().getChildren();
		if (childrenTodo != null) {
			childrenTodo.forEach(child -> todoService.delete(child.getId()));
		}
		todoService.delete(id);

		return "redirect:" + RoutingURL.TODOS_INDEX;
	}
}