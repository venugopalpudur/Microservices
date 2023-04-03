package com.service;

import org.springframework.stereotype.Service;

import com.model.Order;


public interface OrderService {

	public Order addOrder(Order orderId);
	
	public Order updateOrder(int orderId, Order order);
	
	public String deleteOrder(int orderId);

	public Order searchOrder(int orderId);
}
