package com.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.CustomerCart;
import com.model.Product;

import jakarta.transaction.Transactional;


@Repository
public interface CustomerCartRepository extends JpaRepository<CustomerCart, Integer> {
	
	@Transactional
	public CustomerCart findByCustomerId(int customerId);
	
	//public Optional<User> findByUserId(String userId);
}
