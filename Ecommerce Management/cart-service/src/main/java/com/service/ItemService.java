package com.service;

import org.springframework.stereotype.Service;

import com.model.Item;

@Service
public interface ItemService {

	public Item addCustomerAddress();
	
	public Item updateCustomerAddress(int customerId,  Item address);
	
	public String deleteCustomerAddress();
	
	public Item searchCustomerAddress();
	
}
