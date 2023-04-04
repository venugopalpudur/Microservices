package com.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Customer {
	
	private int id;
	
	private String customerId;	
	
	private String customerName;
	
	private String customerEmail;
	
	private Address customerBillingAddress;
	
	private Address customerShippingAddress;

}
