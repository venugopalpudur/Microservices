package com.service;

import org.springframework.stereotype.Service;

import com.model.Product;


public interface ProductService {

	public Product addProduct(Product product);
	
	public Product updateProduct(int productId, Product product);
	
	public String deleteProduct(int productId);

	public Product searchProduct(int productId);
}
