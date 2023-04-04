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

import com.dto.InventoryDto;
import com.dto.ProductDto;
import com.model.Inventory;
import com.model.Product;


@FeignClient("INVENTORY-SERVICE")
public interface InventoryService {
	
	@PostMapping("/inventory")
	public ResponseEntity<Inventory> addInventory(@RequestBody InventoryDto inventory);
	
	@PutMapping("/inventory/{inventoryId}")
	public ResponseEntity<Inventory> updateInventory(@PathVariable("inventoryId") int inventoryId, @RequestBody Inventory inventory);
	
	@GetMapping("/inventory/{inventoryId}")
	public ResponseEntity<Inventory> searchInventory(@PathVariable("inventoryId") int inventoryId);
	
	@DeleteMapping("/inventory/{inventoryId}")
	public ResponseEntity<?> deleteteInventory(@PathVariable("inventoryId") int inventoryId);
}
