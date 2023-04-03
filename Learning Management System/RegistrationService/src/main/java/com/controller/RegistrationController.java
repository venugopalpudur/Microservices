package com.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.config.JwtUtils;
import com.model.ConfirmationToken;
import com.model.User;
import com.service.ConfirmationTokenService;
import com.service.UserService;
import com.util.AuthRequest;
import com.util.AuthResponse;
import com.util.Response;

@RestController
@EnableMethodSecurity(prePostEnabled = true)
public class RegistrationController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private ConfirmationTokenService confirmationTokenService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtUtils jwtUtil;
	
	@Autowired
	AuthResponse authresp;
	
	@GetMapping("/hello")
	public String hello() {
		return "Hello, you have successfully loged in";
	}
	
	@PostMapping("/authenticate")
	public AuthResponse generateToken(@RequestBody AuthRequest authRequest) throws Exception{
		try {
			System.out.println(authRequest.toString());
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
				);
		}catch(Exception ex) {
			ex.printStackTrace();
			//throw new Exception("invalid username/pass");
		}
		authresp.setUsername(authRequest.getUsername());
		authresp.setToken(jwtUtil.generateToken(userService.loadUserByUsername(authRequest.getUsername())));
		return authresp;
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody User user){
		userService.signUp(user);
		Response response = new Response();
		return new ResponseEntity<>("Registered", HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
	@GetMapping("/get")
	public ResponseEntity<?> get(){
		return new ResponseEntity<>(userService.getUser(), HttpStatus.OK); 
	}
	
	@GetMapping("/register/confirm")
	public ResponseEntity<?> confirm(@RequestParam("token") String token){
		Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenService.findConfirmationTokenByToken(token);

		optionalConfirmationToken.ifPresent(userService::confirmUser);
		return new ResponseEntity<>("Account confirmation done !", HttpStatus.OK); 
	}
	
}
