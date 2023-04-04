package com.dto;

import com.model.Address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerDto {
	
	private String customerName;
	
	private String customerEmail;
	
	private Address customerBillingAddress;
	
	private Address customerShippingAddress;

}
