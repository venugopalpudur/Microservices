package com.model;

import com.dto.ProductDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

	private int id;
	
	private String productId;
	
	private String productName;
	
	private String productDescription;
	
	private int price;
	
	private int quantity;

}
