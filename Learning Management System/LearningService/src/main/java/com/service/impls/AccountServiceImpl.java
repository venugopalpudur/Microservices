package com.service.impls;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dto.UserDTO;
import com.entity.DateOf;
import com.feignclient.EmailService;
import com.model.ConfirmationToken;
import com.model.User;
import com.repository.ConfirmationTokenRepository;
import com.repository.UserRepository;
import com.service.AccountService;
import com.service.ConfirmTokenService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import jakarta.mail.Message;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements UserDetailsService, AccountService{
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ConfirmationTokenRepository tokenRepo;
	
	@Autowired
	private ConfirmTokenService tokenService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private EmailService emailService;
	
	private static final String EMAIL_SERVICE = "EMAIL-SERVICE";

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {					
		final Optional<User> optionalUser = userRepo.findByEmail(email);

		return optionalUser.orElseThrow(() -> new UsernameNotFoundException(MessageFormat.format("User with email {0} cannot be found.", email)));		
		//throw new UsernameNotFoundException(MessageFormat.format("User with email {0} cannot be found.", email));
	}
		
	public void signUp(UserDTO userDTO) {
		
		//PasswordEncoder pass = new BCryptPasswordEncoder();
		final String encryptedPassword = bCryptPasswordEncoder.encode(userDTO.getPassword());
		User user = new User();
		user.setName(userDTO.getName());
		user.setEmail(userDTO.getEmail());
		user.setUsername(userDTO.getUsername());
		user.setSurname(userDTO.getSurname());
		user.setPhone(userDTO.getPhone());
		user.setPassword(encryptedPassword);
		user.setRoles(userDTO.getRoles());
		final User createdUser = userRepo.save(user);
		final ConfirmationToken confirmationToken = new ConfirmationToken(user);
		tokenService.saveConfirmationToken(confirmationToken); 
		sendMail(user.getEmail(), confirmationToken.getConfirmationToken());
	}
	
	//@CircuitBreaker(name = EMAIL_SERVICE, fallbackMethod = "sendAvailableMail")
	public void sendMail(String email, String token) {
		emailService.sendConfirmationMail(email, token);
	}
	
	/*public void sendAvailableMail(String email, String token, Exception e) {
	}*/
	   
	public void confirmUser(ConfirmationToken confirmationToken) {
		final User user = confirmationToken.getUser();
		user.setEnabled(true);
		userRepo.save(user);
		tokenService.deleteConfirmationToken(confirmationToken.getId());
	}
	
	public void confirmEMail(ConfirmationToken confirmationToken) {
		final User user = confirmationToken.getUser();
		user.setEnabled(true);
		userRepo.save(user);
		tokenService.deleteConfirmationToken(confirmationToken.getId());
	}

	@Override
	public String accountLocked(String userId) {
		User user = userRepo.findByUserId(userId).get();
		user.setLocked(true);
		userRepo.save(user);
		return "Account Locked";
	}

	@Override
	public String accountExpired() {
		Iterator<User> iterator = userRepo.findAll().iterator();
		while (iterator.hasNext()) {
			User user = iterator.next();
			if (user.getCreatedDate().getYear()+1 == LocalDateTime.now().getYear()) {
				userRepo.delete(user);
			}		// send email for account expire
		}
		return "Account Expired";
	}

	@Override
	public String credentialsExpired() {
		Iterator<User> iterator = userRepo.findAll().iterator();
		while (iterator.hasNext()) {
			User user = iterator.next();
				if (user.getUpdatedDate().get(user.getUpdatedDate().size()-1).getUpdatedTime()
						.getMonthValue()+3 == LocalDateTime.now().getMonthValue()) {
					accountLocked(user.getUserId());
					
				}		// send email for credential expire
				 // we can create custom months change from admin like in 3 == ? with saving in table from admin
		}
		return "Credentials Expired";
	}
}
