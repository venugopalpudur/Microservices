package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.Inventory;
import com.repository.InventoryRepository;

@Service
public class InventoryServiceImpl implements InventoryService{
	
	@Autowired
	private InventoryRepository inventoryRepository;
	
	private static final String EMAIL_SERVICE = "EMAIL-SERVICE";

	@Override
	public Inventory addInventory(Inventory inventory) {
		Inventory inv = inventoryRepository.save(inventory);
		if(inv != null) {
			return inv;
		}
		return null;
	}

	@Override
	public Inventory updateInventory(int inventoryId, Inventory inventory) {
		if(inventoryRepository.findById(inventoryId).isPresent()) {
			
			Inventory inv = inventoryRepository.findById(inventoryId).get();
			inv.setProductId(inventory.getProductId());
			inv.setQuantity(inventory.getQuantity());
			
			Inventory updatedInventory = inventoryRepository.save(inv);
			
			if(updatedInventory != null) {
				return updatedInventory;
			}
			return null;
		}
		return null;
	}

	@Override
	public String deleteInventory(int inventoryId) {
		
		if(inventoryRepository.findById(inventoryId).isPresent()) {
			inventoryRepository.deleteById(inventoryId);
			return "Inventory of id : "+inventoryId+" is deleted.";
		}
		return "cannot be deleted";
	}

	@Override
	public Inventory searchInventory(int inventoryId) {
		if(inventoryRepository.findById(inventoryId).isPresent()) {
			return inventoryRepository.findById(inventoryId).get();
		}
		return null;
	}


}
