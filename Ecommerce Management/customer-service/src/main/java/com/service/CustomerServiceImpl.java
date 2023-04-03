package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.Address;
import com.model.Customer;
import com.repository.CustomerRepository;

@Service
public class CustomerServiceImpl implements CustomerService{
	
	@Autowired
	private CustomerRepository customerRepo;
	
	private static final String EMAIL_SERVICE = "EMAIL-SERVICE";

	@Override
	public Customer addCustomer(Customer customer) {
		Customer cust = customerRepo.save(customer);
		if(cust != null) {
			return cust;
		}
		return null;
	}

	@Override
	public Customer updateCustomer(int customerId, Customer customer) {
		if(customerRepo.findById(customerId).isPresent()) {
			
			Customer cst = customerRepo.findById(customerId).get();
			cst.setCustomerName(customer.getCustomerName());
			cst.setCustomerEmail(customer.getCustomerEmail());
			
			Address add1 = cst.getCustomerBillingAddress();
			add1.setDoorNo(customer.getCustomerBillingAddress().getDoorNo());
			add1.setCity(customer.getCustomerBillingAddress().getCity());
			add1.setLayout(customer.getCustomerBillingAddress().getLayout());
			add1.setStreetName(customer.getCustomerBillingAddress().getStreetName());
			add1.setPincode(customer.getCustomerBillingAddress().getPincode());
			
			Address add2 = cst.getCustomerShippingAddress();
			add1.setDoorNo(customer.getCustomerShippingAddress().getDoorNo());
			add1.setCity(customer.getCustomerShippingAddress().getCity());
			add1.setLayout(customer.getCustomerShippingAddress().getLayout());
			add1.setStreetName(customer.getCustomerShippingAddress().getStreetName());
			add1.setPincode(customer.getCustomerShippingAddress().getPincode());
			
			cst.setCustomerBillingAddress(add1);
			cst.setCustomerShippingAddress(add2);
			
			Customer updatedCustomer = customerRepo.save(cst);
			
			if(updatedCustomer != null) {
				return updatedCustomer;
			}
			return null;
		}
		return null;
	}

	@Override
	public String deleteCustomer(int customerId) {
		
		if(customerRepo.findById(customerId).isPresent()) {
			customerRepo.deleteById(customerId);
			return "customer of id : "+customerId+" is deleted.";
		}
		return "cannot be deleted";
	}

	@Override
	public Customer searchCustomer(int customerId) {
		if(customerRepo.findById(customerId).isPresent()) {
			return customerRepo.findById(customerId).get();
		}
		return null;
	}


}
