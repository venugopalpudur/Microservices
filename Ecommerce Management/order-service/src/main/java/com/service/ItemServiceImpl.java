package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import com.model.Item;
import com.model.Order;
import com.repository.ItemRepository;
import com.repository.OrderRepository;

public class ItemServiceImpl implements ItemService {

	@Autowired
	private ItemRepository addressRepo;
	
	@Autowired
	private OrderRepository customerRepo;

	@Override
	public Item addCustomerAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Item updateCustomerAddress(int customerId, Item address) {
		/*if (customerRepo.findById(customerId).isPresent()) {

			Customer cst = customerRepo.findById(customerId).get();
			cst.setCustomerBillingAddress(customer.getCustomerBillingAddress());
			cst.setCustomerShippingAddress(customer.getCustomerShippingAddress());
			Customer updatedCustomer = customerRepo.save(cst);

			if (updatedCustomer != null) {
				return updatedCustomer;
			}
			return null;
		}*/
		return null;
	}

	@Override
	public String deleteCustomerAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Item searchCustomerAddress() {
		// TODO Auto-generated method stub
		return null;
	}

}
