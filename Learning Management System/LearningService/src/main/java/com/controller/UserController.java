package com.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dto.PasswordDTO;
import com.dto.PaymentDTO;
import com.dto.UserDTO;
import com.exceptions.NoDataFoundException;
import com.exceptions.NullDataException;
import com.feignclient.PaymentService;
import com.funcs.Funcs;
import com.model.User;
import com.service.UserService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/api/v1/user")
@CrossOrigin("http://localhost:4200/")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private Funcs funcs;

	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
	@GetMapping("/getall")
	public ResponseEntity<?> getAll() throws NoDataFoundException{
		
		List<User> users = userService.getUsersList();
		if(!users.isEmpty()) {
			return funcs.generateResponse(users, null, HttpStatus.OK, "List of users");
		}
		else {
			throw new NoDataFoundException("No data found for the users.");
		}	
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping("/getUser")
	public ResponseEntity<?> getUser(Authentication authentication){
		User user = userService.getUser(authentication);
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	 
	
	//@CircuitBreaker(name = "EMAIL-SERVICE", fallbackMethod = "sendAvailableMail")
	@PreAuthorize("hasRole('ROLE_USER')")
	@PutMapping("/update")
	public ResponseEntity<?> updateUser(@RequestBody UserDTO userDTO, Authentication authentication) throws NullDataException {
		User user = userService.updateUser(userDTO, authentication);
		if(user != null) {
			return funcs.generateResponse(Arrays.asList(user), null, HttpStatus.OK, "User updated successfully.");
		}
		else {
			throw new NullDataException("User details cannot be updated.");
		}	
	}
	
	/*public ResponseEntity<?> sendAvailableMail(UserDTO userDTO, Authentication authentication, Exception e) {
		return funcs.generateErrorResponse("Server Error", HttpStatus.SERVICE_UNAVAILABLE, "User updated successfully, but mail cannot be sent, Please try again later with re-update.");
	}*/
	
	
	//@CircuitBreaker(name = "EMAIL-SERVICE", fallbackMethod = "sendAvailablePassMail")
	@PreAuthorize("hasRole('ROLE_USER')")
	@PutMapping("/change-password")
	public ResponseEntity<?> changePassword(@RequestBody PasswordDTO password, Authentication authentication) throws NoDataFoundException {
		String user = userService.changePassword(password, authentication);
		if(!user.isEmpty()) {
			return funcs.generateResponse(null, null, HttpStatus.OK, user);
		}
		else {
			throw new NoDataFoundException("Password cannot be changed");
		}
	} 
	
	/*public ResponseEntity<?> sendAvailablePassMail(PasswordDTO password, Authentication authentication, Exception e) {
		return funcs.generateErrorResponse("Server Error", HttpStatus.SERVICE_UNAVAILABLE, "Password changed successfuly, but maill cannot be sent, Please try again later with re-change of password.");
	}*/
}
