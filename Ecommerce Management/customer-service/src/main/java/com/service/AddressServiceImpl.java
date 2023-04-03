package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import com.model.Address;
import com.model.Customer;
import com.repository.AddressRepository;
import com.repository.CustomerRepository;

public class AddressServiceImpl implements AddressService {

	@Autowired
	private AddressRepository addressRepo;
	
	@Autowired
	private CustomerRepository customerRepo;

	@Override
	public Address addCustomerAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Address updateCustomerAddress(int customerId, Address address) {
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
	public Address searchCustomerAddress() {
		// TODO Auto-generated method stub
		return null;
	}

}
