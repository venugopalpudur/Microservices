package com.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.Payment;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
	public Optional<Payment> findByUserId(String userId);
}
