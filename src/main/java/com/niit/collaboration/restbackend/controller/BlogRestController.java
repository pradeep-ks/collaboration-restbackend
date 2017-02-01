package com.niit.collaboration.restbackend.controller;

import java.util.Date;
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

import com.niit.collaboration.backend.dao.BlogDao;
import com.niit.collaboration.backend.dao.UserDao;
import com.niit.collaboration.backend.model.Blog;
import com.niit.collaboration.backend.model.Comment;
import com.niit.collaboration.backend.model.User;

@RestController
public class BlogRestController {
	@Autowired
	BlogDao blogDao;
	
	@Autowired
	UserDao userDao;
	
	@GetMapping(value = "/blog/new/")
	public ResponseEntity<List<Blog>> getNewBlogs() {
		List<Blog> blogs = blogDao.listNewBlogs();
		if (blogs.isEmpty()) {
			return new ResponseEntity<List<Blog>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Blog>>(blogs, HttpStatus.OK);
	}
	
	@GetMapping(value = "/blog/approved/")
	public ResponseEntity<List<Blog>> getApprovedBlogs() {
		List<Blog> blogs = blogDao.listApprovedBlogs();
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
		blog.setStatus("NEW");
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
		b.setStatus(blog.getStatus());
		
		blogDao.update(b);
		
		return new ResponseEntity<Blog>(b, HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/blog/{id}")
	public ResponseEntity<Blog> deleteBlog(@PathVariable("id") long id) {
		Blog blog = blogDao.getBlogById(id);
		if (blog == null) {
			return new ResponseEntity<Blog>(HttpStatus.NOT_FOUND);
		}
		blogDao.remove(blog);
		
		return new ResponseEntity<Blog>(HttpStatus.OK);
	}
	
	@PutMapping(value = "/blog/approve/{id}")
	public ResponseEntity<Blog> approveBlog(@PathVariable("id") long id) {
		Blog blog = blogDao.getBlogById(id);
		if (blog == null) {
			return new ResponseEntity<Blog>(HttpStatus.NOT_FOUND);
		}
		blog.setStatus("APPROVED");
		blogDao.update(blog);
		return new ResponseEntity<Blog>(blog, HttpStatus.OK);
	}
	
	@PutMapping(value = "/blog/reject/{id}")
	public ResponseEntity<Blog> rejectBlog(@PathVariable("id") long id) {
		Blog blog = blogDao.getBlogById(id);
		if (blog == null) {
			return new ResponseEntity<Blog>(HttpStatus.NOT_FOUND);
		}
		blog.setStatus("REJECTED");
		blogDao.update(blog);
		return new ResponseEntity<Blog>(blog, HttpStatus.OK);
	}
	
	@PostMapping(value = "/blog/comment/{blogId}")
	public ResponseEntity<Comment> makeComment(@PathVariable("blogId") long blogId, @RequestBody Comment comment, HttpSession session) {
		Blog blog = blogDao.getBlogById(blogId);
		long userId = (Long) session.getAttribute("loggedInUserId");
		User user = userDao.getUserById(userId);
		Comment comm = new Comment();
		comm.setBlog(blog);
		comm.setUser(user);
		comm.setCommentedOn(new Date());
		comm.setComments(comment.getComments());
		
		blogDao.makeComment(comm);
		return new ResponseEntity<Comment>(comm, HttpStatus.OK);
	}
	
	@GetMapping(value = "/blog/comment/{blogId}")
	public ResponseEntity<List<Comment>> getCommentsOnBlog(@PathVariable("blogId") long blogId) {
		List<Comment> comments = blogDao.getCommentsByBlogId(blogId);
		if (comments.isEmpty()) {
			return new ResponseEntity<List<Comment>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Comment>>(comments, HttpStatus.OK);
	}
}
