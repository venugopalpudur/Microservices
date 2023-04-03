package com.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("EMAIL-SERVICE")
public interface EmailService {
	
	@GetMapping("/send-registration-confirmation-mail")
	public void sendConfirmationMail(@RequestParam("userMail") String userMail, @RequestParam("token") String token);
}
