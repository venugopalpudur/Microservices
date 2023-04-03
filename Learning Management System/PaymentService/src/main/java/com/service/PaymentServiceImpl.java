package com.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import com.feignclient.EmailService;
import com.model.Payment;
import com.repository.PaymentRepository;
import com.service.PaymentService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private EmailService emailService;

	@Override
	public Payment savePayment(Payment payment) {
		if (payment != null) {
			Random random = new Random();
			payment.setOtp(random.nextInt(999999));
			Payment pay = paymentRepository.save(payment);
			if (pay != null) {
				sendMail(payment.getEmail(), payment.getOtp());
			}
			return pay;
		}
		return null;
	}

	@Override
	public void sendMail(String email, int otp) {
		emailService.sendPaymentOTPMail(email, otp);
	}

	@Override
	public boolean confirmPayment(int otp, Integer userId) {
		if (userId != null  && otp != 0) {
			Payment payment = paymentRepository.findById(userId).get();
			if (payment.getOtp() == otp) {
				payment.setIsConfirm(true);
				paymentRepository.save(payment);
				return true;
			}
			return false;
		}
		return false;
	}

	@Override
	public boolean getPayment(Integer payId, String userId, String courseId) {
		Payment payment = paymentRepository.findById(payId).get();
		if (payment != null && payment.getUserId().compareTo(userId) == 0 && payment.getCourseId().compareTo(courseId) == 0) {
			boolean pay = true;
			return pay;
		}
		return false;
	}

}
