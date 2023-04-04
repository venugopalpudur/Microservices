package com.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.dto.CustomerDto;
import com.dto.PasswordDTO;
import com.dto.RoleDTO;
import com.dto.UserDTO;
import com.feignclient.EmailService;
import com.funcs.Funcs;
import com.model.ConfirmationToken;
import com.model.Roles;
import com.model.User;
import com.repository.ConfirmationTokenRepository;
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
				final String encryptedPassword = bCryptPasswordEncoder.encode(password.getConfirmNewPassowrd());
				System.out.println(user.getPassword());
				user.setPassword(encryptedPassword); 
				System.out.println(user.getPassword());
				// mail verification [DONE] // send mail to user
				sendPassMail(user.getEmail(), user.getName(), user.getUserId());				
				funcs.sendNotify("Password changed", user);

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

	
	@Override
	public String assignRoles(String userId, RoleDTO roleDTO, Authentication authentication) {
		UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
		// System.out.println(userRepository.findByEmail(userPrincipal.getUsername()).orElseThrow(()
		// -> new UsernameNotFoundException(MessageFormat.format("User with email {0}
		// cannot be found.", "agsdg"))));
		//for (GrantedAuthority authority : userPrincipal.getAuthorities()) {
		//Iterator itr = userPrincipal.getAuthorities().iterator();
		 //while(itr.hasNext()) {
			 //GrantedAuthority authority = (GrantedAuthority) itr.next();
		for (Iterator iterator = userPrincipal.getAuthorities().iterator(); iterator.hasNext();) {
			GrantedAuthority authority = (GrantedAuthority) iterator.next();
			System.out.println(authority);
			
		
			//System.out.println( userPrincipal.getAuthorities().toString());
			if (authority.getAuthority().compareTo("ROLE_ADMIN") == 0
					|| authority.getAuthority().compareTo("ROLE_MODERATOR") == 0) {
				User user = userRepository.findByUserId(userId).get();
				if (user.getRoles().isEmpty()) {
					userRepository.save(funcs.setRoles(user, roleDTO));
					// send mail & notifications to both user and admin
					funcs.sendNotify("You profile has been assigned with role : " + roleDTO.getUserRole()
							+ ". by the admin : " + userPrincipal.getUsername(), user);
					funcs.sendNotify(
							"You assigned with role : " + roleDTO.getUserRole() + " to the user : "
									+ userPrincipal.getUsername(),
							userRepository.findByEmail(userPrincipal.getUsername()).get());

					return "Role is set for user " + user.getName();
				} else {
					for (Roles role : user.getRoles()) {
						if (role.getUserRole().compareTo(roleDTO.getUserRole()) != 0) {
							userRepository.save(funcs.setRoles(user, roleDTO));
							// send mail
							funcs.sendNotify("You profile has been assigned with role : " + roleDTO.getUserRole()
									+ ". by the admin : " + userPrincipal.getUsername(), user);
							funcs.sendNotify(
									"You assigned with role : " + roleDTO.getUserRole() + " to the user : "
											+ userPrincipal.getUsername(),
									userRepository.findByEmail(userPrincipal.getUsername()).get());

							return "Role is set for user " + user.getName();
						}
					}
				}
				return "Your are admin";
			} 
		}
		return null;
	}

	@Override
	public String revokeRoles(String userId, RoleDTO roleDTO, Authentication authentication) {
		UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
		for (GrantedAuthority authority : userPrincipal.getAuthorities()) {
			if (authority.getAuthority().compareTo("ROLE_ADMIN") == 0
					|| authority.getAuthority().compareTo("ROLE_MODERATOR") == 0) {
				User user = userRepository.findByUserId(userId).get();
				if (user.getRoles().isEmpty()) {
					return "No mentioned Role was set for user " + user.getName();
				} else {
					List<Roles> roles = new ArrayList<>();
					Iterator<Roles> iterator = user.getRoles().iterator();
					while (iterator.hasNext()) {
						Roles role = iterator.next();
						if (role.getUserRole().compareTo(roleDTO.getUserRole()) == 0) {
							iterator.remove();
							Authentication auth = SecurityContextHolder.getContext().getAuthentication();
							List<GrantedAuthority> updatedAuthorities = auth.getAuthorities().stream()
									.filter(r -> !role.getUserRole().equals(r.getAuthority()))
									.collect(Collectors.toList());
							Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(),
									auth.getCredentials(), updatedAuthorities);
							SecurityContextHolder.getContext().setAuthentication(newAuth);
							break;
						}
						roles.add(role);
					}
					user.setRoles(roles);
					userRepository.save(user);

					funcs.sendNotify("You profile has been revoked with role : " + roleDTO.getUserRole()
							+ ". by the admin : " + userPrincipal.getUsername(), user);
					funcs.sendNotify(
							"You revoked with role : " + roleDTO.getUserRole() + " to the user : "
									+ userPrincipal.getUsername(),
							userRepository.findByEmail(userPrincipal.getUsername()).get());

					// send mail & notifications to both user and admin
					return "Role is deleted for user " + user.getName();
				}
			}
			return null;
		}
		return null;
	}
}
