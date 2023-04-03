package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.Item;
import com.model.Cart;
import com.repository.CartRepository;

@Service
public class CartServiceImpl implements CartService{
	
	@Autowired
	private CartRepository cartRepository;
	
	private static final String EMAIL_SERVICE = "EMAIL-SERVICE";

	@Override
	public Cart addCart(Cart cart) {
		Cart ct = cartRepository.save(cart);
		if(ct != null) {
			return ct;
		}
		return null;
	}

	@Override
	public Cart updateCart(int cartId, Cart cart) {
		if(cartRepository.findById(cartId).isPresent()) {	
			Cart cst = cartRepository.findById(cartId).get();
			cst.setItems(cart.getItems());
			Cart updatedCart = cartRepository.save(cst);
			if(updatedCart != null) {
				return updatedCart;
			}
			return null;
		}
		return null;
	}

	@Override
	public String deleteCart(int cartId) {
		
		if(cartRepository.findById(cartId).isPresent()) {
			cartRepository.deleteById(cartId);
			return "Cart of id : "+cartId+" is deleted.";
		}
		return "cannot be deleted";
	}

	@Override
	public Cart searchCart(int cartId) {
		if(cartRepository.findById(cartId).isPresent()) {
			return cartRepository.findById(cartId).get();
		}
		return null;
	}


}
