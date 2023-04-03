package com.service;

import org.springframework.stereotype.Service;

import com.model.Address;

@Service
public interface AddressService {

	public Address addCustomerAddress();
	
	public Address updateCustomerAddress(int customerId,  Address address);
	
	public String deleteCustomerAddress();
	
	public Address searchCustomerAddress();
	
}
