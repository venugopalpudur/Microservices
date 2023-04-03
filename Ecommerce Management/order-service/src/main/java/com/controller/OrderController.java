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
import com.model.Order;
import com.repository.OrderRepository;
import com.service.OrderService;

@RestController
public class OrderController {

	@Autowired
	private OrderService cartService;
	
	@Autowired
	private OrderRepository orderRepository;

	@PostMapping("/order")
	public ResponseEntity<?> addOrder(@RequestBody Order order) throws NoDataFoundException {
		Order od = cartService.addOrder(order);
		if (od != null) {
			return new ResponseEntity<>(od, HttpStatus.OK);
		} else {
			throw new NoDataFoundException("Something went wrong. customer cannot be created");
		}
	}

	@PutMapping("/order/{orderId}")
	public ResponseEntity<?> updateOrder(@PathVariable("cartId") int orderId, @RequestBody Order order)
			throws NoDataFoundException {
		Order ct = cartService.updateOrder(orderId, order);
		if (ct != null) {
			return new ResponseEntity<>(ct, HttpStatus.OK);
		} else {
			throw new NoDataFoundException("Something went wrong. Customer cannot be updated");
		}
	}

	@GetMapping("/order/{orderId}")
	public ResponseEntity<?> searchOrder(@PathVariable("orderId") int orderId) throws NoDataFoundException {
		Order ct = cartService.searchOrder(orderId);
		if (ct != null) {
			return new ResponseEntity<>(ct, HttpStatus.OK);
		} else {
			throw new NoDataFoundException("Something went wrong. customer details cannot be found");
		}
	}

	@DeleteMapping("/order/{orderId}")
	public ResponseEntity<?> deleteteOrder(@PathVariable("orderId") int orderId) {

		return new ResponseEntity<>(cartService.deleteOrder(orderId), HttpStatus.OK);

	}
	
	@GetMapping("/order/all")
	public ResponseEntity<?> allOrder() throws NoDataFoundException {
		List<Order> od = orderRepository.findAll();
		if (od != null) {
			return new ResponseEntity<>(od, HttpStatus.OK);
		} else {
			throw new NoDataFoundException("Something went wrong. Order details cannot be found");
		}
	}

}
