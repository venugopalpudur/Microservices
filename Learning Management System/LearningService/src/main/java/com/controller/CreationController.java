package com.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dto.CourseDTO;
import com.entity.Course;
import com.exceptions.NoDataFoundException;
import com.exceptions.NullDataException;
import com.funcs.Funcs;
import com.model.User;
import com.service.CreationService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/api/v1/course")
@CrossOrigin("http://localhost:4200/")
public class CreationController {

	@Autowired
	private CreationService creationService;
	
	@Autowired
	private Funcs funcs;	 
	

	
	@PreAuthorize("hasRole('ROLE_TEACHER')")
	@PostMapping("/create")
	public ResponseEntity<?> addCourse(@RequestBody CourseDTO courseDTO, Authentication authentication) throws NoDataFoundException {
		// exceptions, validations (if-else), 
		Course addCourse = creationService.addCourse(courseDTO, authentication);
		if(addCourse != null) {
			return funcs.generateResponse(null, Arrays.asList(addCourse), HttpStatus.OK, "Course added successfully.");
		}
		else {
			throw new NoDataFoundException("Something went wrong. Course cannot be created");
		}
	}
	
	
	@GetMapping("/getall")
	public ResponseEntity<?> getAllCourses() throws NoDataFoundException{
		
		List<Course> courses = new ArrayList<>();		
		Iterator<Course> iterator = creationService.getAllCourses().iterator();
		while (iterator.hasNext()) {
			Course course = iterator.next();
			//course.setDescription(null);
			course.setSubscriber(null);
			courses.add(course);
		}
		if(!courses.isEmpty()) {
			return funcs.generateResponse(null, courses, HttpStatus.OK, "List of courses");
		}
		else {
			throw new NoDataFoundException("Something went wrong.");
		}
	}

	
	/*@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
	@GetMapping("/course/get/{userId}")
	public ResponseEntity<?> getCourses(@PathVariable("userId") String userId){
		
		List<Course> courses = creationService.getCourses(userId);
				
		return new ResponseEntity<>((List<Course>)courses, HttpStatus.OK);
	}*/
	
	/*@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
	@GetMapping("/course/getRegisterCourse/{userId}")
	public ResponseEntity<?> getRegisteredCourses(@PathVariable("userId") String userId){
		
		List<Course> courses = creationService.getRegisteredCourses(userId);
				
		return new ResponseEntity<>((List<Course>)courses, HttpStatus.OK);
	}*/
	
	@PreAuthorize("hasRole('ROLE_TEACHER')")
	@GetMapping("/getSubscribers")
	public ResponseEntity<?> getSubscribers(@RequestParam("courseId") String courseId, Authentication authentication) throws NoDataFoundException{
		
		List<User> users = creationService.getSubscribers(courseId, authentication);
		if(!users.isEmpty()) {
			return funcs.generateResponse(users, null, HttpStatus.OK, "List of users as a subscriber");
		}
		else {
			throw new NoDataFoundException("Something went wrong.");
		}
	}
	
	@PreAuthorize("hasRole('ROLE_TEACHER')")
	@PutMapping("/update")
	public ResponseEntity<?> updateCourse(@RequestBody CourseDTO courseDTO,@RequestParam("courseId") String courseId, Authentication authentication) throws NullDataException {
				
		Course courses = creationService.updateCourse(courseDTO, courseId, authentication);
		if(courses != null) {
			return funcs.generateResponse(null, Arrays.asList(courses), HttpStatus.OK, "Course updated successfully.");
		}
		else {
			throw new NullDataException("Course cannot be updated");
		}
	}
	
	@PreAuthorize("hasRole('ROLE_TEACHER')")
	@DeleteMapping("/delete")
	public ResponseEntity<?> deleteCourse(@RequestParam("courseId") String courseId, Authentication authentication) throws NoDataFoundException {
		
		String delete = creationService.deleteCourse(courseId, authentication);
		if(!delete.isEmpty()) {
			return funcs.generateResponse(null, null, HttpStatus.OK, delete);
		}
		else {
			throw new NoDataFoundException("Course cannot be deleted.");
		}
	}
	
	@PreAuthorize("hasRole('ROLE_TEACHER')")
	@DeleteMapping("/deleteall")
	public ResponseEntity<?> deleteAllCourses(Authentication authentication) throws NoDataFoundException {
		
		String delete = creationService.deleteAllCourses(authentication);
		if(!delete.isEmpty()) {
			return funcs.generateResponse(null, null, HttpStatus.OK, delete);
		}
		else {
			throw new NoDataFoundException("Courses cannot be deleted.");
		}
	}
}



