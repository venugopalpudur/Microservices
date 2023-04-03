package com.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exceptions.NoPaymentFoundException;
import com.model.ErrorDTO;
import com.model.Payment;
import com.service.PaymentService;
import com.service.PaymentServiceImpl;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;


@RestController
//@RequestMapping("/api/v1/payment")
@CrossOrigin("http://localhost:4200/")
public class PaymentController { 
 
	@Autowired
	private PaymentService paymentService;
	
	@CircuitBreaker(name = "EMAIL-SERVICE", fallbackMethod = "sendAvailable")
	@PostMapping("/pay")
	public ResponseEntity<?> savePayment(@RequestBody Payment payment) throws Exception{
		Payment pay = paymentService.savePayment(payment);
		if(pay != null) {
			return new ResponseEntity<>(pay.getId(), HttpStatus.OK);
		}
		else {
			throw new NoPaymentFoundException("Payment details cannot be saved.");
		}
	}
	
	public ResponseEntity<?> sendAvailable(Payment payment, Exception e) {
		ErrorDTO errorDTO = new ErrorDTO();
		errorDTO.setErrorType(HttpStatus.SERVICE_UNAVAILABLE.name());
		errorDTO.setException("Server Unavailable at the moment, Please try again later.");
		return new ResponseEntity<>(errorDTO, HttpStatus.OK);
	}
	
	
	@GetMapping("/getPayment")
	public boolean getPayment(@RequestParam("payId") int payId, @RequestParam("userId") String userId, @RequestParam("courseId") String courseId) throws Exception{
		boolean pay = paymentService.getPayment(payId, userId, courseId);
		if(pay) {
			return pay;
		}
		else {
			return false;
		}
	}
	
	@GetMapping("/confirmPayment") // userid = payid
	public ResponseEntity<?> confirm(@RequestParam("otp") int otp, @RequestParam("userId") int userId) throws Exception{
		if(paymentService.confirmPayment(otp, userId)) {
			return new ResponseEntity<>(true, HttpStatus.OK);
		}
		else {
			throw new NoPaymentFoundException("Something went wrong, Please try again later.");
		}
	}
	
	
	
	
	
	/*@CircuitBreaker(name = "EMAIL-SERVICE", fallbackMethod = "sendAvailableMail")
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody UserDTO userDTO){
		
		// for admin registration send mail and get confirmation from there
						
		accountService.signUp(userDTO);
		return funcs.generateResponse(null, null, HttpStatus.OK, "Registration successfull.");
	}
	
	public ResponseEntity<?> sendAvailableMail(UserDTO userDTO, Exception e) {
		return funcs.generateErrorResponse("Server Error", HttpStatus.SERVICE_UNAVAILABLE, "Server Unavailable at the moment, Please try again later.");
	}*/
		

	 
}
