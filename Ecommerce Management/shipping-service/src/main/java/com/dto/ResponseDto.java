package com.dto;

import java.util.List;

import com.model.Cart;
import com.model.Customer;
import com.model.CustomerCart;
import com.model.CustomerOrder;
import com.model.Inventory;
import com.model.InventoryProduct;
import com.model.Order;
import com.model.Product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDto {
	
	private Inventory inventory;
	
	private Product product;
	
	private Customer customer;
	
	private CustomerCart customerCart;
	
	private Cart cart;
	
	private CustomerOrder customerOrder;
	
	private InventoryProduct inventoryProduct;
	
	private Order order;
	
	private List<Order> viewAllOrders;
	
	private List<CustomerOrder> viewCustomerOrders;
}
