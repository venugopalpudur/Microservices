package com.service;

import java.text.MessageFormat;
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

import com.feignclient.EmailService;
import com.model.ConfirmationToken;
import com.model.User;
import com.repository.ConfirmationTokenRepository;
import com.repository.UserRepository;

import jakarta.mail.Message;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private ConfirmationTokenRepository tokenRepo;
	
	@Autowired
	private ConfirmationTokenService tokenService;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private EmailService emailService;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {					
		final Optional<User> optionalUser = userRepo.findByEmail(email);

		return optionalUser.orElseThrow(() -> new UsernameNotFoundException(MessageFormat.format("User with email {0} cannot be found.", email)));		
		//throw new UsernameNotFoundException(MessageFormat.format("User with email {0} cannot be found.", email));
	}
	
	public List<User> getUser() {
		return userRepo.findAll();
	}
	
	public void signUp(User user) {
		
		//PasswordEncoder pass = new BCryptPasswordEncoder();
		final String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
		
		user.setPassword(encryptedPassword);
		final User createdUser = userRepo.save(user);
		final ConfirmationToken confirmationToken = new ConfirmationToken(user);
		tokenService.saveConfirmationToken(confirmationToken); 
		emailService.sendConfirmationMail(user.getEmail(), confirmationToken.getConfirmationToken());
	}
	
	public void confirmUser(ConfirmationToken confirmationToken) {
		final User user = confirmationToken.getUser();
		user.setEnabled(true);
		userRepo.save(user);
		tokenService.deleteConfirmationToken(confirmationToken.getId());	
	}
}
