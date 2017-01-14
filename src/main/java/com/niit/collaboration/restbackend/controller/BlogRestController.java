package com.niit.collaboration.restbackend.controller;

import java.util.Date;
import java.util.List;

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

import com.niit.collaboration.backend.dao.BlogDao;
import com.niit.collaboration.backend.model.Blog;

@RestController
public class BlogRestController {
	@Autowired
	BlogDao blogDao;
	
	@GetMapping(value = "/blog/")
	public ResponseEntity<List<Blog>> getBlogs() {
		List<Blog> blogs = blogDao.list();
		if (blogs.isEmpty()) {
			return new ResponseEntity<List<Blog>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Blog>>(blogs, HttpStatus.OK);
	}

	@GetMapping(value = "/blog/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Blog> getBlog(@PathVariable("id") long id) {
		Blog blog = blogDao.getBlogById(id);
		if (blog == null) {
			return new ResponseEntity<Blog>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Blog>(blog, HttpStatus.OK);
	}
	
	@PostMapping(value = "/blog/")
	public ResponseEntity<Void> createBlog(@RequestBody Blog blog) {
		blog.setDateCreated(new Date());
		blogDao.create(blog);
		return new ResponseEntity<Void>(HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/blog/{id}")
	public ResponseEntity<Blog> updateBlog(@PathVariable("id") long id, @RequestBody Blog blog) {
		Blog b = blogDao.getBlogById(id);
		if (b == null) {
			return new ResponseEntity<Blog>(HttpStatus.NOT_FOUND);
		}
		
		b.setTitle(blog.getTitle());
		b.setContent(blog.getContent());
		b.setUser(blog.getUser());
		
		blogDao.update(b);
		
		return new ResponseEntity<Blog>(b, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/blog/{id}")
	public ResponseEntity<Blog> deleteBlog(@PathVariable("id") long id) {
		Blog blog = blogDao.getBlogById(id);
		if (blog == null) {
			return new ResponseEntity<Blog>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Blog>(HttpStatus.OK);
	}
}
