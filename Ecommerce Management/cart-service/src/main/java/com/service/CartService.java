package com.service;

import org.springframework.stereotype.Service;

import com.model.Cart;


public interface CartService {

	public Cart addCart(Cart cart);
	
	public Cart updateCart(int cartId, Cart cart);
	
	public String deleteCart(int cartId);

	public Cart searchCart(int cartId);
}
