package com.ikeda.todolist.Services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ikeda.todolist.Models.Place;
import com.ikeda.todolist.Repositories.PlaceRepository;

@Service
public class PlaceService {
	@Autowired
	PlaceRepository placeRepository;

	public List<Place> findAll() {
		return placeRepository.findAll();
	}

	public Optional<Place> find(Long id) {
		return placeRepository.findById(id);
	}

	@Transactional
	public Place findForUpdate(Long id) {
		return placeRepository.findForUpdateById(id);
	}

	public Place save(Place place) {
		return placeRepository.save(place);
	}

	public void delete(Long id) {
		placeRepository.deleteById(id);
	}
}
