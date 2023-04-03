package com.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.dto.PaymentDTO;


@FeignClient("PAYMENT-SERVICE")
public interface PaymentService {
	
	@PostMapping("/pay")
	public ResponseEntity<?> savePayment(@RequestBody PaymentDTO payment);
	
	@GetMapping("/getPayment")
	public boolean getPayment(@RequestParam("payId") int payId, @RequestParam("userId") String userId, @RequestParam("courseId") String courseId);
	
	@GetMapping("/confirmPayment")
	public ResponseEntity<?> confirm(@RequestParam("otp") int otp, @RequestParam("userId") String userId);
	
}
