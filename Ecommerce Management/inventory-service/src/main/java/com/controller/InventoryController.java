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
import com.model.Inventory;
import com.repository.InventoryRepository;
import com.service.InventoryService;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
public class InventoryController {

	@Autowired
	private InventoryService inventoryService;
	
	@Autowired
	private InventoryRepository inventoryRepository;

	@PostMapping("/inventory")
	public ResponseEntity<?> addInventory(@RequestBody Inventory inventory) throws NoDataFoundException {
		Inventory inv = inventoryService.addInventory(inventory);
		if (inv != null) {
			return new ResponseEntity<>(inv, HttpStatus.OK);
		} else {
			throw new NoDataFoundException("Something went wrong. customer cannot be created");
		}
	}

	@PutMapping("/inventory/{inventoryId}")
	public ResponseEntity<?> updateInventory(@PathVariable("inventoryId") int inventoryId, @RequestBody Inventory inventory)
			throws NoDataFoundException {
		Inventory inv = inventoryService.updateInventory(inventoryId, inventory);
		if (inv != null) {
			return new ResponseEntity<>(inv, HttpStatus.OK);
		} else {
			throw new NoDataFoundException("Something went wrong. Customer cannot be updated");
		}
	}

	@GetMapping("/inventory/{inventoryId}")
	public ResponseEntity<?> searchInventory(@PathVariable("inventoryId") int inventoryId) throws NoDataFoundException {
		Inventory inv = inventoryService.searchInventory(inventoryId);
		if (inv != null) {
			return new ResponseEntity<>(inv, HttpStatus.OK);
		} else {
			throw new NoDataFoundException("Something went wrong. customer details cannot be found");
		}
	}

	@DeleteMapping("/inventory/{inventoryId}")
	public ResponseEntity<?> deleteteInventory(@PathVariable("inventoryId") int inventoryId) {

		return new ResponseEntity<>(inventoryService.deleteInventory(inventoryId), HttpStatus.OK);

	}

	@GetMapping("/inventory/all")
	public ResponseEntity<?> allInventory() throws NoDataFoundException {
		List<Inventory> inv = inventoryRepository.findAll();
		if (inv != null) {
			return new ResponseEntity<>(inv, HttpStatus.OK);
		} else {
			throw new NoDataFoundException("Something went wrong. Inventory details cannot be found");
		}
	}
}
