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

import com.dto.ProductDto;
import com.model.Product;


@FeignClient("PRODUCT-SERVICE")
public interface ProductService {
	
	@PostMapping("/products")
	public ResponseEntity<Product> addProduct(@RequestBody ProductDto product);
	
	@PutMapping("/products/{productId}")
	public ResponseEntity<Product> updateProduct(@PathVariable("productId") int productId, @RequestBody Product product);
	
	@GetMapping("/products/{productId}")
	public ResponseEntity<Product> searchProduct(@PathVariable("productId") int productId);
	
	@DeleteMapping("/products/{productId}")
	public ResponseEntity<?> deleteteProduct(@PathVariable("productId") int productId);
}
