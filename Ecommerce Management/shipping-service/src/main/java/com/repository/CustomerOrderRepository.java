package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.CustomerCart;
import com.model.CustomerOrder;
import com.model.Product;

import jakarta.transaction.Transactional;


@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Integer> {

	@Transactional
	public List<CustomerOrder> findByCustomerId(int customerId);
	
	@Transactional
	public List<CustomerOrder> findByOrderId(int customerId);
}
