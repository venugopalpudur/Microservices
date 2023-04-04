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

import com.dto.OrderDto;
import com.dto.ProductDto;
import com.model.Order;
import com.model.Product;


@FeignClient("ORDER-SERVICE")
public interface OrderService {
	
	@PostMapping("/order")
	public ResponseEntity<Order> addOrder(@RequestBody OrderDto order);
	
	@PutMapping("/order/{orderId}")
	public ResponseEntity<Order> updateOrder(@PathVariable("orderId") int orderId, @RequestBody Order order);
	
	@GetMapping("/order/{orderId}")
	public ResponseEntity<Order> searchOrder(@PathVariable("orderId") int orderId);
	
	@DeleteMapping("/order/{orderId}")
	public ResponseEntity<?> deleteteOrder(@PathVariable("orderId") int orderId);
}
