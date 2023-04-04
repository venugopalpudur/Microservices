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

import com.dto.CartDto;
import com.dto.ProductDto;
import com.model.Cart;
import com.model.Product;


@FeignClient("CART-SERVICE")
public interface CartService {
	
	@PostMapping("/cart")
	public ResponseEntity<Cart> addCart(@RequestBody CartDto cart);
	
	@PutMapping("/cart/{cartId}")
	public ResponseEntity<Cart> updateCart(@PathVariable("cartId") int cartId, @RequestBody Cart cart);
	
	@GetMapping("/cart/{cartId}")
	public ResponseEntity<Cart> searchCart(@PathVariable("cartId") int cartId);
	
	@DeleteMapping("/cart/{cartId}")
	public ResponseEntity<?> deleteteCustomer(@PathVariable("cartId") int cartId);
}
