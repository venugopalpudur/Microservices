package com.service;

import com.dto.CartDto;
import com.dto.CustomerDto;
import com.dto.ProductDto;
import com.dto.ResponseDto;


public interface ShoppingService {

	public ResponseDto createProduct(ProductDto product);
	
	public ResponseDto createCustomer(CustomerDto customer);
	
	public ResponseDto addProductsToCart(int customerId, CartDto cart);

	public ResponseDto placeOrder(int customerId);
	
	public ResponseDto viewAllOrders(int customerId);
}
