package com.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@FeignClient("EMAIL-SERVICE")
public interface EmailService {
	
	@GetMapping("/send-registration-confirmation-mail")
	public void sendConfirmationMail(@RequestParam("userMail") String userMail, @RequestParam("token") String token);

	@GetMapping("/send-user-update-mail")
	public void sendUserUpdateMail(@RequestParam("userMail") String userMail, @RequestParam("name") String name, @RequestParam("userId") String userId);

	@GetMapping("/send-password-change-mail")
	public void sendPasswordChangeMail(@RequestParam("userMail") String userMail, @RequestParam("name") String name, @RequestParam("userId") String userId);

	@GetMapping("/send-mail-confirmation-mail")
	public void sendMailConfirmationMail(@RequestParam("userMail") String userMail, @RequestParam("token") String token);
}
