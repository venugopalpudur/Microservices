package com.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@FeignClient("EMAIL-SERVICE")
public interface EmailService {
	
	@GetMapping("/send-payment-otp")
	public void sendPaymentOTPMail(@RequestParam("userMail") String userMail, @RequestParam("otp") int otp);
}
