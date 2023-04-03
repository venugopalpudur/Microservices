package com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exceptions.NoDataFoundException;
import com.model.Cart;
import com.repository.CartRepository;
import com.service.CartService;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
public class CartController {

	@Autowired
	private CartService cartService;
	
	@Autowired
	private CartRepository cartRepository;

	@PostMapping("/cart")
	public ResponseEntity<?> addCart(@RequestBody Cart cart) throws NoDataFoundException {
		Cart ct = cartService.addCart(cart);
		if (ct != null) {
			return new ResponseEntity<>(ct, HttpStatus.OK);
		} else {
			throw new NoDataFoundException("Something went wrong. customer cannot be created");
		}
	}

	@PutMapping("/cart/{cartId}")
	public ResponseEntity<?> updateCart(@PathVariable("cartId") int cartId, @RequestBody Cart cart)
			throws NoDataFoundException {
		Cart ct = cartService.updateCart(cartId, cart);
		if (ct != null) {
			return new ResponseEntity<>(ct, HttpStatus.OK);
		} else {
			throw new NoDataFoundException("Something went wrong. Customer cannot be updated");
		}
	}

	@GetMapping("/cart/{cartId}")
	public ResponseEntity<?> searchCart(@PathVariable("cartId") int cartId) throws NoDataFoundException {
		Cart ct = cartService.searchCart(cartId);
		if (ct != null) {
			return new ResponseEntity<>(ct, HttpStatus.OK);
		} else {
			throw new NoDataFoundException("Something went wrong. customer details cannot be found");
		}
	}

	@DeleteMapping("/cart/{cartId}")
	public ResponseEntity<?> deleteteCustomer(@PathVariable("cartId") int cartId) {

		return new ResponseEntity<>(cartService.deleteCart(cartId), HttpStatus.OK);

	}

	@GetMapping("/cart/all")
	public ResponseEntity<?> allCart() throws NoDataFoundException {
		List<Cart> ca = cartRepository.findAll();
		if (ca != null) {
			return new ResponseEntity<>(ca, HttpStatus.OK);
		} else {
			throw new NoDataFoundException("Something went wrong. Cart details cannot be found");
		}
	}
}
