package com.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Item {
	
	private Integer id;
	
	private String itemId;
	
	private int productId;
	
	private String productName;
	
	private int quantity;
	
	private int price;

}
