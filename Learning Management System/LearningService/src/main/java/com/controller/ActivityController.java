package com.controller;

import java.awt.ActiveEvent;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.web.exchanges.HttpExchange.Principal;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dto.PaymentDTO;
import com.dto.ResponseDTO;
import com.dto.ReviewDTO;
import com.dto.RoleDTO;
import com.dto.UserDTO;
import com.entity.Course;
import com.exceptions.NoDataFoundException;
import com.exceptions.NullDataException;
import com.feignclient.PaymentService;
import com.funcs.Funcs;
import com.service.ActivityService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin("http://localhost:4200/")
public class ActivityController {
	
	@Autowired
	private ActivityService activityService;
	 
	@Autowired
	private Funcs funcs;
	
	@CircuitBreaker(name = "PAYMENT-SERVICE", fallbackMethod = "sendAvailable")
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostMapping("/pay")
	public ResponseEntity<?> savePayment(@RequestBody PaymentDTO payment, @RequestParam("courseId") String courseId, Authentication authentication) throws NoDataFoundException{
		if(payment != null) {
			return new ResponseEntity<>(activityService.savePayment(payment, courseId, authentication), HttpStatus.OK);
		}
		else {
			throw new NoDataFoundException("Payment details cannot be saved.");
		}
	}
	
	public ResponseEntity<?> sendAvailable(PaymentDTO payment, String courseId, Authentication authentication, Exception e) {
		return funcs.generateErrorResponse("Server Error", HttpStatus.SERVICE_UNAVAILABLE, "Server Unavailable at the moment, Please try again later.");
	}
	
	
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/subscribe")
	public ResponseEntity<?> subscribeCourse(@RequestParam("courseId") String courseId, @RequestParam("payId") int payId, Authentication authentication) throws NullDataException {
		
		Course subscribe = activityService.subscribeCourse(courseId, payId, authentication);
		if(subscribe != null) {
			return funcs.generateResponse(null, Arrays.asList(subscribe), HttpStatus.OK, "Course subscribed successfully.");
		}
		else {
			throw new NullDataException("Cannot be enrolled with course.");
		} 
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/unsubscribe")
	public ResponseEntity<?> unSubscribeCourse(@RequestParam("courseId") String courseId, Authentication authentication) throws NoDataFoundException {
		
		String unsubscribe = activityService.unSubscribeCourse(courseId, authentication);
		if(!unsubscribe.isEmpty()) {
			return funcs.generateResponse(null, null, HttpStatus.OK, unsubscribe);
		}
		else {
			throw new NoDataFoundException("Cannot be un-enrolled with course.");
		}  
	}
	
	@PreAuthorize("hasRole('ROLE_STUDENT')")
	@PostMapping("/review")
	public ResponseEntity<?> addReviewToCourse(@RequestParam("courseId") String courseId, @RequestBody ReviewDTO review, Authentication authentication) throws NoDataFoundException{
		
		List<Course> courses = activityService.addReviewToCourse(courseId, review, authentication);
		if(!courses.isEmpty()) {
			return funcs.generateResponse(null, courses, HttpStatus.OK, "Review added successfully.");
		}
		else {
			throw new NoDataFoundException("Something went wrong. Please try again after some time.");
		}
	}
	
	@PreAuthorize("hasRole('ROLE_STUDENT')")
	@PutMapping("/updateReview")
	public ResponseEntity<?> updateReviewToCourse(@RequestParam("reviewId") int reviewId, @RequestParam("courseId") String courseId,  @RequestBody ReviewDTO review, Authentication authentication) throws NoDataFoundException {

		List<Course> courses = activityService.updateReviewToCourse(reviewId, courseId, review, authentication);
		if(!courses.isEmpty()) {
			return funcs.generateResponse(null, courses, HttpStatus.OK, "Review updated successfully.");
		}
		else {
			throw new NoDataFoundException("Something went wrong. Please try again after some time.");
		}
	}
	
	@PreAuthorize("hasRole('ROLE_STUDENT')")
	@DeleteMapping("/deleteReview")
	public ResponseEntity<?> deleteReview(@RequestParam("reviewId") int reviewId, Authentication authentication) throws NoDataFoundException {
		
		String review = activityService.deleteReview(reviewId, authentication);
		if(!review.isEmpty()) {
			return funcs.generateResponse(null, null, HttpStatus.OK, review);
		}
		else {
			throw new NoDataFoundException("Something went wrong. Please try again after some time.");
		}
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/assignRole")
	public ResponseEntity<?> assignRoles(@RequestParam("userId") String userId, @RequestBody RoleDTO roleDTO, Authentication authentication) throws NoDataFoundException {
		
		String role = activityService.assignRoles(userId, roleDTO, authentication);
		if(!role.isEmpty()) {
			return funcs.generateResponse(null, null, HttpStatus.OK, role);
		}
		else {
			throw new NoDataFoundException("Something went wrong. Please try again after some time.");
		}
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/revokeRole")
	public ResponseEntity<?> revokeRoles(@RequestParam("userId") String userId, @RequestBody RoleDTO roleDTO, Authentication authentication) throws NoDataFoundException {
		
		String role = activityService.revokeRoles(userId, roleDTO, authentication);
		if(!role.isEmpty()) {
			return funcs.generateResponse(null, null, HttpStatus.OK, role);
		}
		else {
			throw new NoDataFoundException("Something went wrong. Please try again after some time.");
		}
	}
	

}
