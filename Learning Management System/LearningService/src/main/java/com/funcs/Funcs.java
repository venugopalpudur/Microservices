package com.funcs;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.dto.ErrorDTO;
import com.dto.ResponseDTO;
import com.dto.RoleDTO;
import com.entity.Course;
import com.entity.Notifications;
import com.model.Roles;
import com.model.User;
import com.repository.NotificationsRepository;
import com.repository.UserRepository;

@Component
public class Funcs {
	
	@Autowired
	private NotificationsRepository notificationsRepository;
	
	@Autowired
	private ResponseDTO responseDTO;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ErrorDTO errorDTO;

	public void sendNotify(String msg, User user) {		
		List<Notifications> notifications = new ArrayList<>();
		notifications.addAll(user.getNotifications());
		notifications.add(new Notifications(msg));
		user.setNotifications(notifications);
		userRepository.save(user);		
	}
	
	// send notifications to all users or students for suggestions
	
	public void sendSuggestNotify() {
		
	}
	
	public User setRoles(User user, RoleDTO roleDTO) {
		Roles setRoles = new Roles();
		setRoles.setUserRole(roleDTO.getUserRole());
		setRoles.setDescription(roleDTO.getDescription());
		List<Roles> roles = new ArrayList<>();
		roles.addAll(user.getRoles());
		roles.add(setRoles);
		user.setRoles(roles);
		return user;
	}
	
	public ResponseEntity<?> generateResponse(List<User> users, List<Course> courses, HttpStatus status, String details){
		responseDTO.setTimestamp(LocalDateTime.now());
		responseDTO.setHttpStatusCode(status.value());
		responseDTO.setDetails(details);
		responseDTO.setUsers(users);
		responseDTO.setCourses(courses);
		return new ResponseEntity<>(responseDTO, status);
	}
	
	public ResponseEntity<?> generateErrorResponse(String type, HttpStatus status, String msg){
		errorDTO.setErrorType(type);
		errorDTO.setException(msg);
		errorDTO.setStatusCode(status.value());
		errorDTO.setTime(LocalDateTime.now());
		return new ResponseEntity<>(errorDTO, status);
	}	
}
