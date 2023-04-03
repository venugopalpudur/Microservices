package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exceptions.NoDataFoundException;
import com.model.Customer;
import com.repository.CustomerRepository;
import com.service.CustomerService;

@RestController
public class CustomerController {

	@Autowired
	private CustomerService customerService;
	
	@Autowired
	private CustomerRepository customerRepository;

	@PostMapping("/addCustomer")
	public ResponseEntity<?> addCustomer(@RequestBody Customer customer) throws NoDataFoundException {
		Customer cust = customerService.addCustomer(customer);
		if (cust != null) {
			return new ResponseEntity<>(cust, HttpStatus.OK);
		} else {
			throw new NoDataFoundException("Something went wrong. customer cannot be created");
		}
	}

	@PutMapping("/updateCustomer/{customerId}")
	public ResponseEntity<?> updateCustomer(@PathVariable("customerId") int customerId, @RequestBody Customer customer)
			throws NoDataFoundException {
		Customer cust = customerService.updateCustomer(customerId, customer);
		if (cust != null) {
			return new ResponseEntity<>(cust, HttpStatus.OK);
		} else {
			throw new NoDataFoundException("Something went wrong. Customer cannot be updated");
		}
	}

	@GetMapping("/searchCustomer/{customerId}")
	public ResponseEntity<?> searchCustomer(@PathVariable("customerId") int customerId) throws NoDataFoundException {
		Customer cust = customerService.searchCustomer(customerId);
		if (cust != null) {
			return new ResponseEntity<>(cust, HttpStatus.OK);
		} else {
			throw new NoDataFoundException("Something went wrong. customer details cannot be found");
		}
	}

	@DeleteMapping("/deleteteCustomer/{customerId}")
	public ResponseEntity<?> deleteteCustomer(@PathVariable("customerId") int customerId) {

		return new ResponseEntity<>(customerService.deleteCustomer(customerId), HttpStatus.OK);

	}

	@GetMapping("/customer/all")
	public ResponseEntity<?> allCustomers() throws NoDataFoundException {
		List<Customer> cu = customerRepository.findAll();
		if (cu != null) {
			return new ResponseEntity<>(cu, HttpStatus.OK);
		} else {
			throw new NoDataFoundException("Something went wrong. Customer details cannot be found");
		}
	}
}
