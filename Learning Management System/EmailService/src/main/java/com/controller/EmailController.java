package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.service.EmailSenderService;

@RestController
public class EmailController {
 
	@Autowired  
	public EmailSenderService emailSenderService;
	
	@GetMapping("/send-registration-confirmation-mail")
	public void sendConfirmationMail(@RequestParam("userMail") String userMail, @RequestParam("token") String token) {
				
		final SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(userMail);
		mailMessage.setFrom("upload02.sdcard@gmail.com");
		mailMessage.setSubject("Mail Confirmation Link"); //http://localhost:8080/api/v1/auth/register/confirm?token=
		mailMessage.setText(
				"Thank you for registering. Please click on the below link to activate your account." + "http://localhost:4200/account/confirm?token="
						+ token);
				
		emailSenderService.sendEmail(mailMessage);
	}
	
	@GetMapping("/send-payment-otp")
	public void sendPaymentOTPMail(@RequestParam("userMail") String userMail, @RequestParam("otp") int otp) {
				
		final SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(userMail);
		mailMessage.setFrom("upload02.sdcard@gmail.com");
		mailMessage.setSubject("Confirm your payment"); //http://localhost:8080/api/v1/auth/register/confirm?token=
		mailMessage.setText(
				"Please put the following otp to confirm your payment = " + otp);
				
		emailSenderService.sendEmail(mailMessage);
	}
	
	@GetMapping("/send-mail-confirmation-mail")
	public void sendMailConfirmationMail(@RequestParam("userMail") String userMail, @RequestParam("token") String token) {
				
		final SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(userMail);
		mailMessage.setFrom("upload02.sdcard@gmail.com");
		mailMessage.setSubject("Confirm e-Mail ID");
		mailMessage.setText(
				"e-Mail ID changed successfully. Please click on the below link to confirm your account e-Mail ID changes." + "http://localhost:8080/api/v1/auth/confirm/e-mail?token="
						+ token);
				
		emailSenderService.sendEmail(mailMessage);
	}
	
	@GetMapping("/send-user-update-mail")
	public void sendUserUpdateMail(@RequestParam("userMail") String userMail, @RequestParam("name") String name, @RequestParam("userId") String userId) {
		final SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(userMail);
		mailMessage.setFrom("upload02.sdcard@gmail.com");
		mailMessage.setSubject("User details updated !");
		mailMessage.setText(
				"Hi "+name+","
				+"User details with userID :"+userId+" have been updated with personal details."+
				"If you haven't changed your details or suspect account compromised ? "+
				"Change your password immediately to protect your account - http://localhost:8080/user/change-password"				
				);
		emailSenderService.sendEmail(mailMessage);
	}
	
	@GetMapping("/send-password-change-mail")
	public void sendPasswordChangeMail(@RequestParam("userMail") String userMail, @RequestParam("name") String name, @RequestParam("userId") String userId) {
		final SimpleMailMessage mailMessage = new SimpleMailMessage();
		mailMessage.setTo(userMail);
		mailMessage.setFrom("upload02.sdcard@gmail.com");
		mailMessage.setSubject("Password Changed !");
		mailMessage.setText(
				"Hi "+name+","
				+"User details with userID :"+userId+" have been updated with new password."+
				"If you haven't changed your details or suspect account compromised ? "+
				"Change your password immediately to protect your account - http://localhost:8080/user/change-password"				
				);
		emailSenderService.sendEmail(mailMessage);
	}
}
