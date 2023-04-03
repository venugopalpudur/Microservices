package com.service;

import org.springframework.stereotype.Service;

import com.model.Customer;


public interface CustomerService {

	public Customer addCustomer(Customer customer);
	
	public Customer updateCustomer(int customerId, Customer customer);
	
	public String deleteCustomer(int customerId);

	public Customer searchCustomer(int customerId);
}
