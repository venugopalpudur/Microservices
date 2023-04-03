package com.service;

import org.springframework.stereotype.Service;

import com.model.Inventory;


public interface InventoryService {

	public Inventory addInventory(Inventory customer);
	
	public Inventory updateInventory(int customerId, Inventory customer);
	
	public String deleteInventory(int customerId);

	public Inventory searchInventory(int customerId);
}
