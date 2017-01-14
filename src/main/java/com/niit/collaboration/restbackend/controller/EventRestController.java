package com.niit.collaboration.restbackend.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.niit.collaboration.backend.dao.EventDao;
import com.niit.collaboration.backend.model.Event;

@RestController
public class EventRestController {

	@Autowired
	EventDao eventDao;
	
	@GetMapping(value = "/event/")
	public ResponseEntity<List<Event>> getEvents() {
		List<Event> events = eventDao.listEvents();
		if (events.isEmpty()) {
			return new ResponseEntity<List<Event>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Event>>(events, HttpStatus.OK);
	}
	
	@GetMapping(value = "/event/{eventId}")
	public ResponseEntity<Event> getEvent(@PathVariable("eventId") long eventId) {
		Event event = eventDao.getEventById(eventId);
		if (event == null) {
			return new ResponseEntity<Event>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Event>(event, HttpStatus.OK);
	}
	
	@PostMapping(value = "/event/")
	public ResponseEntity<Event> createEvent(@RequestBody Event event) {
		eventDao.create(event);
		return new ResponseEntity<Event>(event, HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/event/{eventId}")
	public ResponseEntity<Event> updateEvent(@PathVariable("eventId") long eventId, @RequestBody Event evt) {
		Event e = eventDao.getEventById(eventId);
		if (e == null) {
			return new ResponseEntity<Event>(HttpStatus.NOT_FOUND);
		}
		
		e.setDescription(evt.getDescription());
		e.setEventDate(evt.getEventDate());
		e.setStatus(evt.getStatus());
		e.setTitle(evt.getTitle());
		e.setVenue(evt.getVenue());
		eventDao.update(e);
		
		return new ResponseEntity<Event>(e, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/event/{eventId}")
	public ResponseEntity<Event> deleteEvent(@PathVariable("eventId") long eventId) {
		Event evt = eventDao.getEventById(eventId);
		if (evt == null) {
			return new ResponseEntity<Event>(HttpStatus.NOT_FOUND);
		}
		eventDao.remove(evt);
		return new ResponseEntity<Event>(HttpStatus.OK);
	}
}
