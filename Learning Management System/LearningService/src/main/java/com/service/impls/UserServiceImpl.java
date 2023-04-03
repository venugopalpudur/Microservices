package com.service.impls;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dto.PasswordDTO;
import com.dto.UserDTO;
import com.entity.Course;
import com.feignclient.EmailService;
import com.funcs.Funcs;
import com.model.ConfirmationToken;
import com.model.User;
import com.repository.ConfirmationTokenRepository;
import com.repository.CourseRepository;
import com.repository.UserRepository;
import com.service.ConfirmTokenService;
import com.service.UserService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
		
	@Autowired
	private ConfirmTokenService tokenService;
	
	@Autowired
	private CourseRepository courseRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private Funcs funcs;
	
	private static final String EMAIL_SERVICE = "EMAIL-SERVICE";
	
	@Override
	public User updateUser(UserDTO userDTO, Authentication authentication) {
				
		UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
		Optional<User> userOp = userRepository.findByEmail(userPrincipal.getUsername());
		if(userOp.isPresent() && userDTO != null) {
			User user = userOp.get();
			List<Course> courses = courseRepository.findByInstructor(user);
			user.setName(userDTO.getName());
			user.setSurname(userDTO.getSurname());
			user.setUsername(userDTO.getUsername());
			user.setEmail(userDTO.getEmail());
			user.setPhone(userDTO.getPhone());
			
			System.out.println(user.getUsername());
			
			if(!userDTO.getEmail().isEmpty()) { // send mail to user [DONE]
				user.setEnabled(false);
				final ConfirmationToken confirmationToken = new ConfirmationToken(user);
				tokenService.saveConfirmationToken(confirmationToken); 
				sendMailConfirmMail(user.getEmail(), confirmationToken.getConfirmationToken());				
			}
			sendMail(user.getEmail(), user.getName(), user.getUserId());
			funcs.sendNotify("User account details updated", user); // send notification [DONE]
			
			List<Course> toUpdateCourses = new ArrayList<>();
			for(Course course : courses) {
				course.setInstructor(user);
				toUpdateCourses.add(course);  // when updating user also update course instructor
			}
			courseRepository.saveAll(toUpdateCourses); 		/// password not allowed to update here	
			
			return userRepository.save(user); 
		}
		return null;
	}
	
	//@CircuitBreaker(name = EMAIL_SERVICE, fallbackMethod = "sendAvailableMail")
	public void sendMail(String email, String userName, String userId) {
		emailService.sendUserUpdateMail(email, userName, userId);
	}

	//@CircuitBreaker(name = EMAIL_SERVICE, fallbackMethod = "sendAvailableConfirmMail")
	public void sendMailConfirmMail(String email, String token) {
		emailService.sendMailConfirmationMail(email, token);
	}
	
	//@CircuitBreaker(name = EMAIL_SERVICE, fallbackMethod = "sendAvailablePassMail")
	public void sendPassMail(String email, String userName, String userId) {
		emailService.sendPasswordChangeMail(email, userName, userId);
	}
	
	/*public void sendAvailableMail(String email, String userName, String userId, Exception e) {
	}
	public void sendAvailableConfirmMail(String email, String userName, String userId, Exception e) {
	}
	public void sendAvailablePassMail(String email, String userName, String userId, Exception e) {
	}
	*/
	
	
	
	@Override
	public List<User> getUsersList() {
		return userRepository.findAll();
	}

	@Override
	public String changePassword(PasswordDTO password, Authentication authentication) {
				
		UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
		Optional<User> userOp = userRepository.findByEmail(userPrincipal.getUsername());
		if(userOp.isPresent() && password != null) {			
			if(password.getOldPassword().compareTo(password.getConfirmNewPassowrd()) != 0 && password.getNewPassword().compareTo(password.getConfirmNewPassowrd()) == 0) {
				User user = userOp.get();
				System.out.println(password.toString());
				List<Course> courses = courseRepository.findByInstructor(user);
				final String encryptedPassword = bCryptPasswordEncoder.encode(password.getConfirmNewPassowrd());
				System.out.println(user.getPassword());
				user.setPassword(encryptedPassword); 
				System.out.println(user.getPassword());
				List<Course> toUpdateCourses = new ArrayList<>();
				for(Course course : courses) {
					course.setInstructor(user);
					toUpdateCourses.add(course);
				}
				// mail verification [DONE] // send mail to user
				sendPassMail(user.getEmail(), user.getName(), user.getUserId());				
				funcs.sendNotify("Password changed", user);
				
				courseRepository.saveAll(toUpdateCourses);
				userRepository.save(user);
				return "Password Upated";
			}
			return "Please use different Password";
		}	
		// only password is updated
		return null;
	}
	
	@Override
	public User getUser(Authentication authentication) {
		UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
		Optional<User> userOp = userRepository.findByEmail(userPrincipal.getUsername());
		return userOp.get();
	}

}
