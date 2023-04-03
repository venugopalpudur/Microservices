package com.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.dto.PasswordDTO;
import com.dto.UserDTO;
import com.model.User;

//@Service
public interface UserService {
	
	public List<User> getUsersList();
	
	public User getUser(Authentication authentication);
		
	public User updateUser(UserDTO userDTO, Authentication authentication);
	
	public String changePassword(PasswordDTO password, Authentication authentication);
}
