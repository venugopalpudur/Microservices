package com.service;

import org.springframework.web.bind.annotation.RequestParam;

import com.model.Payment;

//@Service
public interface PaymentService {
	
	public Payment savePayment(Payment payment);
	
	public void sendMail(String email, int otp);
	
	public boolean confirmPayment(int otp, Integer userId);
	
	public boolean getPayment(Integer payId, String userId, String courseId);
}
