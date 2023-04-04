package com.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.dto.CustomerDto;
import com.dto.ProductDto;
import com.model.Customer;
import com.model.Product;


@FeignClient("CUSTOMER-SERVICE")
public interface CustomerService {
	
	@PostMapping("/addCustomer")
	public ResponseEntity<Customer> addCustomer(@RequestBody CustomerDto customer);
	
	@PutMapping("/updateCustomer/{customerId}")
	public ResponseEntity<Customer> updateCustomer(@PathVariable("customerId") int customerId, @RequestBody Customer customer);
	
	@GetMapping("/searchCustomer/{customerId}")
	public ResponseEntity<Customer> searchCustomer(@PathVariable("customerId") int customerId);
	
	@DeleteMapping("/deleteteCustomer/{customerId}")
	public ResponseEntity<?> deleteteCustomer(@PathVariable("customerId") int customerId);
}
