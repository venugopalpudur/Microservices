package com.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.config.JwtUtils;
import com.dto.loginDTO;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.std.EnumDeserializer;
import com.funcs.Funcs;
import com.dto.authDTO;
import com.dto.ResponseDTOs;
import com.dto.UserDTO;
import com.model.ConfirmationToken;
import com.model.User;
import com.service.ConfirmTokenService;
import com.service.UserService;
import com.service.AccountServiceImpl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@EnableMethodSecurity(prePostEnabled = true)
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:4200/")
public class AuthenticationController {

	@Autowired
	private AccountServiceImpl accountService;

	@Autowired
	private UserService userService;

	@Autowired
	private ConfirmTokenService confirmationTokenService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtils jwtUtil;

	@Autowired
	authDTO authresp;

	@Autowired
	private Funcs funcs;

	/*
	 * @GetMapping("/hello") public String hello() { return
	 * "Hello, you have successfully loged in"; }
	 */

	@PostMapping("/authenticate")
	public authDTO generateToken(@RequestBody loginDTO authRequest) throws Exception {
		try {
			System.out.println(authRequest.toString());
			Authentication auth = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
			System.out.println(auth.isAuthenticated());
			if (auth.isAuthenticated()) {
				authresp.setUsername(authRequest.getUsername());
				authresp.setToken(jwtUtil.generateToken(accountService.loadUserByUsername(authRequest.getUsername())));
				return authresp;

			} else {
				throw new UsernameNotFoundException("Invalid user credintials / User not present.");
			}

		}catch (Exception ex) {
			ex.printStackTrace(); // throw new throw new
			throw new Exception("Invalid user credintials / User not present / Please verify your email ID");
		}
		/*catch (UsernameNotFoundException ex) {
			ex.printStackTrace(); // throw new throw new
			throw new UsernameNotFoundException("invalid username/pass");
		}
		catch (DisabledException ex) {
			ex.printStackTrace(); // throw new throw new
			throw new DisabledException("Please activate your email-address.");
		}*/

	}

	// @CircuitBreaker(name = "EMAIL-SERVICE", fallbackMethod = "sendAvailableMail")
	//@JsonDeserialize(using = EnumDeserializer.class)
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody UserDTO userDTO) {

		// for admin registration send mail and get confirmation from there
		System.out.println(userDTO);
		accountService.signUp(userDTO);
		return funcs.generateResponse(null, null, HttpStatus.OK, "Registration successfull.");
	}

	/*
	 * public ResponseEntity<?> sendAvailableMail(UserDTO userDTO, Exception e) {
	 * return funcs.generateErrorResponse("Server Error",
	 * HttpStatus.SERVICE_UNAVAILABLE,
	 * "Server Unavailable at the moment, Please try again later."); }
	 */

	@GetMapping("/register/confirm")
	public ResponseEntity<?> confirm(@RequestParam("token") String token) {
		Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenService
				.findConfirmationTokenByToken(token);

		optionalConfirmationToken.ifPresent(accountService::confirmUser);
		return funcs.generateResponse(null, null, HttpStatus.OK, "Registration successfull.");
		// return new ResponseEntity<>("Account confirmation done !", HttpStatus.OK);
	}

	@GetMapping("/confirm/e-mail")
	public ResponseEntity<?> confirmEmail(@RequestParam("token") String token) {
		Optional<ConfirmationToken> optionalConfirmationToken = confirmationTokenService
				.findConfirmationTokenByToken(token);

		optionalConfirmationToken.ifPresent(accountService::confirmEMail);
		return funcs.generateResponse(null, null, HttpStatus.OK, "E-mail confirmation done !");
	}

}
