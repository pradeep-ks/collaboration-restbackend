package com.niit.collaboration.restbackend.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.niit.collaboration.backend.dao.JobDao;
import com.niit.collaboration.backend.dao.UserDao;
import com.niit.collaboration.backend.model.AppliedJob;
import com.niit.collaboration.backend.model.Job;

@RestController
public class JobRestController {

	@Autowired
	JobDao jobDao;
	
	@Autowired
	UserDao userDao;
	
	@GetMapping(value = "/job/")
	public ResponseEntity<List<Job>> getAllJobs() {
		List<Job> jobs = jobDao.listJobs();
		if (jobs.isEmpty()) {
			return new ResponseEntity<List<Job>>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<Job>>(jobs, HttpStatus.OK);
	}
	
	@GetMapping(value = "/job/{jobId}")
	public ResponseEntity<Job> getJobById(@PathVariable("jobId") long jobId) {
		Job job = jobDao.getJobById(jobId);
		if (job == null) {
			return new ResponseEntity<Job>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<Job>(job, HttpStatus.OK);
	}
	
	@PostMapping(value = "/job/")
	public ResponseEntity<Job> addJob(@RequestBody Job job) {
		jobDao.add(job);
		return new ResponseEntity<Job>(job, HttpStatus.CREATED);
	}
	
	@PutMapping(value = "/job/{jobId}")
	public ResponseEntity<Job> updateJob(@PathVariable("jobId") long jobId, @RequestBody Job job) {
		Job j = jobDao.getJobById(jobId);
		if (j == null) {
			return new ResponseEntity<Job>(HttpStatus.NOT_FOUND);
		}
		
		j.setTitle(job.getTitle());
		j.setDescription(job.getDescription());
		j.setDateTime(job.getDateTime());
		j.setLocation(job.getLocation());
		j.setQualification(job.getQualification());
		j.setStatus(job.getStatus());
		j.setVacancy(job.getVacancy());
		
		jobDao.update(j);
		return new ResponseEntity<Job>(j, HttpStatus.OK);
	}
	
	@PostMapping(value = "/job/apply/{jobId}")
	public ResponseEntity<Job> applyForJob(@PathVariable("jobId") long jobId, HttpSession session) {
		long loggedInUserId = (Long) session.getAttribute("loggedInUserId");
		AppliedJob applyJob = new AppliedJob();
		applyJob.setJob(jobDao.getJobById(jobId));
		applyJob.setStatus("NEW");
		applyJob.setDateApplied(new java.util.Date());
		applyJob.setUser(userDao.getUserById(loggedInUserId));
		
		jobDao.saveAppliedJob(applyJob);
		
		return new ResponseEntity<Job>(HttpStatus.CREATED);
	}
}
