package com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dto.CartDto;
import com.dto.CustomerDto;
import com.dto.ProductDto;
import com.dto.ResponseDto;
import com.exceptions.NoDataFoundException;
import com.service.ShoppingService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;

@RestController
@EnableMethodSecurity(prePostEnabled = true)
@RequestMapping("/api/shoppingservice")
@CrossOrigin(origins = "http://localhost:4200/")
public class ShoppingController {

	@Autowired
	private ShoppingService shoppingService;
	
	private int attempt = 1;

	@PostMapping("/products")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	//@CircuitBreaker(name = "CREATE-PRODUCT", fallbackMethod = "createProductFallBack")
	@Retry(name = "CREATE-PRODUCT", fallbackMethod = "createProductFallBack")
	public ResponseEntity<?> createProduct(@RequestBody ProductDto product) throws NoDataFoundException {
		ResponseDto response = shoppingService.createProduct(product);
		if (response.getProduct() != null) {
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			throw new NoDataFoundException("Something went wrong. Product cannot be created");
		}
	}
	
	public ResponseEntity<?> createProductFallBack(@RequestBody ProductDto product, Exception e){
		return new ResponseEntity<>("Something went wrong ! \nProduct cannot be created. Please try after sometime.", HttpStatus.OK);
	}

	@PostMapping("/customer")
	@PreAuthorize("hasRole('ROLE_USER')")
	//@CircuitBreaker(name = "CREATE-CUSTOMER", fallbackMethod = "createCustomerFallBack")
	@Retry(name = "CREATE-CUSTOMER", fallbackMethod = "createCustomerFallBack")
	public ResponseEntity<?> createCustomer(@RequestBody CustomerDto customer) throws NoDataFoundException {
		ResponseDto response = shoppingService.createCustomer(customer);
		if (response.getCustomer() != null) {
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			throw new NoDataFoundException("Something went wrong. Customer cannot be created");
		}
	}
	
	public ResponseEntity<?> createCustomerFallBack(@RequestBody CustomerDto customer, Exception e){
		return new ResponseEntity<>("Something went wrong ! \nCustomer cannot be created. Please try after sometime.", HttpStatus.OK);
	}

	@PutMapping("/customer/{customerId}/cart")
	@PreAuthorize("hasRole('ROLE_USER')")
	//@CircuitBreaker(name = "ADD-TO-CART", fallbackMethod = "addToCartFallBack")
	@Retry(name = "ADD-TO-CART", fallbackMethod = "addToCartFallBack")
	public ResponseEntity<?> addProductsToCart(@PathVariable("customerId") int customerId, @RequestBody CartDto cart)
			throws NoDataFoundException {
		ResponseDto response = shoppingService.addProductsToCart(customerId, cart);
		if (response.getCart() != null) {
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			throw new NoDataFoundException("Something went wrong. Product cannot be added to cart");
		}
	}
	
	public ResponseEntity<?> addToCartFallBack(@PathVariable("customerId") int customerId, @RequestBody CartDto cart, Exception e){
		return new ResponseEntity<>("Something went wrong ! \nProducts cannot be added to cart. Please try after sometime.", HttpStatus.OK);
	}

	@GetMapping("/customer/{customerId}/order") // post to get
	@PreAuthorize("hasRole('ROLE_USER')")
	//@CircuitBreaker(name = "PLACE-ORDER", fallbackMethod = "placeOrderFallBack")
	@Retry(name = "PLACE-ORDER", fallbackMethod = "placeOrderFallBack")
	public ResponseEntity<?> placeOrder(@PathVariable("customerId") int customerId) throws NoDataFoundException {
		ResponseDto response = shoppingService.placeOrder(customerId);
		if (response.getOrder() != null) {
			return new ResponseEntity<>(response, HttpStatus.OK);
		} else {
			throw new NoDataFoundException("Something went wrong. Order cannot be placed");
		}
	}
	
	public ResponseEntity<?> placeOrderFallBack(@PathVariable("customerId") int customerId, Exception e){
		return new ResponseEntity<>("Something went wrong ! \nOrder cannot be placed. Please try after sometime.", HttpStatus.OK);
	}

	@GetMapping("/customer/{customerId}/orders")
	//@PreAuthorize("hasRole('ROLE_USER')")
	//@CircuitBreaker(name = "ORDERS-VIEW", fallbackMethod = "viewAllOrdersFallBack")
	@Retry(name = "ORDERS-VIEW", fallbackMethod = "viewAllOrdersFallBack")
	public ResponseEntity<?> viewAllOrders(@PathVariable("customerId") int customerId) throws NoDataFoundException {
		ResponseDto response = shoppingService.viewAllOrders(customerId);
		//System.out.println("retry method called "+attempt+" times at "+new Date());
		if(response.getViewAllOrders() != null) {
			return new ResponseEntity<>(response, HttpStatus.OK);
		}else {
			throw new NoDataFoundException("No Order details present.");
		}
	}
	
	public ResponseEntity<?> viewAllOrdersFallBack(@PathVariable("customerId") int customerId, Exception e){
		return new ResponseEntity<>("Something went wrong ! \nOrders cannot be shown. Please try after sometime.", HttpStatus.OK);
	}
}
