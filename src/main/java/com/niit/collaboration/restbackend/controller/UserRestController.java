package com.niit.collaboration.restbackend.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.niit.collaboration.backend.dao.UserDao;
import com.niit.collaboration.backend.model.User;

@RestController
public class UserRestController {

	@Autowired
	UserDao userDao;
	
	@GetMapping(value = "/user/")
	public ResponseEntity<List<User>> getAllUsers() {
		System.out.println("Inside UserController::getAllUsers()....");
		List<User> users = userDao.list();
		if (users.isEmpty()) {
			return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<User>>(users, HttpStatus.OK);
	}
	
	@GetMapping(value = "/user/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> getUser(@PathVariable("id") long id) {
		System.out.println("Inside UserController::getUser()....");
		User user = userDao.getUserById(id);
		if (user == null) {
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@GetMapping(value = "/user/uname/{uname}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> getUser(@PathVariable("uname") String username) {
		System.out.println("Inside UserController::getUser(uname)....");
		User user = userDao.getUserByUsername(username);
		if (user == null) {
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@PostMapping(value = "/user/")
	public ResponseEntity<Void> createUser(@RequestBody User newUser) {
		System.out.println("Inside UserController::createUser()....");
		boolean exists = userDao.isExistingUser(newUser);
		if (exists) {
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
		
		newUser.setRole("STUDENT");
		newUser.setActive(true);
		
		userDao.create(newUser);
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/user/{id}")
	public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody User user) {
		User u = userDao.getUserById(id);
		if (u == null) {
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
		u.setUsername(user.getUsername());
		u.setRole(user.getRole());
		u.setPassword(user.getPassword());
		u.setActive(user.isActive());
		u.setEmail(user.getEmail());
		userDao.udpate(u);
		return new ResponseEntity<User>(u, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/user/{id}")
	public ResponseEntity<User> deleteUser(@PathVariable("id") long id) {
		User u = userDao.getUserById(id);
		if (u == null) {
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
		
		userDao.remove(u);
		
		return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
	}
	
	@PostMapping(value = "/user/auth")
	public ResponseEntity<User> authenticate(@RequestBody User user, HttpSession session) {
		boolean result = userDao.authenticate(user.getUsername(), user.getPassword());
		if (result) {
			User u = userDao.getUserByUsername(user.getUsername());
			session.setAttribute("loggedInUser", u);
			session.setAttribute("loggedInUserId", u.getUserId());
			
			return new ResponseEntity<User>(u, HttpStatus.OK);
		}
		return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
	}
}
