package com.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Inventory {
	
	private int id;
	
	private String inventoryId;	
	
	private int productId;
	
	private int quantity;

}
