package com.ikeda.todolist.Controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ikeda.todolist.Constants.RoutingURL;
import com.ikeda.todolist.Models.Place;
import com.ikeda.todolist.Services.PlaceService;

@Controller
@EnableAutoConfiguration
@RequestMapping(RoutingURL.PLACES)
public class PlaceController {
	@Autowired
	PlaceService placeService;

	@RequestMapping(value = RoutingURL.INDEX, method = RequestMethod.GET)
	public String index(Model model) {
		List<Place> places = placeService.findAll();
		model.addAttribute("places", places);
		model.addAttribute("link_to_show", RoutingURL.PLACES_SHOW);
		model.addAttribute("link_to_new", RoutingURL.PLACES_NEW);
		model.addAttribute("link_to_edit", RoutingURL.PLACES_EDIT);
		model.addAttribute("link_to_destroy", RoutingURL.PLACES_DESTROY);

		return "places/index";
	}

	@RequestMapping(value = { RoutingURL.NEW, RoutingURL.NEW + "/{parent_id}" }, method = RequestMethod.GET)
	public String newPlace(Model model, @PathVariable(name = "parent_id", required = false) Optional<Long> parent_id) {
		Place place = new Place();

		model.addAttribute("placeForm", place);
		if (parent_id.isPresent()) {
			model.addAttribute("link_to_create", RoutingURL.PLACES_CREATE + "/" + parent_id.get().toString());
		} else {
			model.addAttribute("link_to_create", RoutingURL.PLACES_CREATE);
		}

		return "places/new";
	}

	@RequestMapping(value = { RoutingURL.CREATE, RoutingURL.CREATE + "/{parent_id}" }, method = RequestMethod.POST)
	public String create(@ModelAttribute Place placeForm,
			@PathVariable(name = "parent_id", required = false) Optional<Long> parent_id) {
		placeService.save(placeForm);

		return "redirect:" + RoutingURL.PLACES_INDEX;
	}

	@RequestMapping(value = RoutingURL.EDIT + "/{id}", method = RequestMethod.GET)
	public String edit(Model model, @PathVariable("id") Long id) {
		Place place = placeService.find(id).get();

		model.addAttribute("placeForm", place);
		model.addAttribute("link_to_update", RoutingURL.PLACES_PESSIMISM_UPDATE);

		return "places/edit";
	}
	
	@RequestMapping(value = RoutingURL.PESSIMISM_UPDATE + "/{id}", method = RequestMethod.PUT)
	public String pessimismUpdate(@ModelAttribute Place placeForm, @PathVariable("id") Long id) {
		
		try {
			placeService.findForUpdate(id);
		} catch (PessimisticLockingFailureException e) {
			return "redirect:" + RoutingURL.PLACES_INDEX;
		}

		placeService.save(placeForm);

		return "redirect:" + RoutingURL.PLACES_INDEX;
	}

	@RequestMapping(value = RoutingURL.DESTROY + "/{id}", method = RequestMethod.DELETE)
	public String destroy(@PathVariable("id") Long id) {
		placeService.delete(id);

		return "redirect:" + RoutingURL.PLACES_INDEX;
	}
}