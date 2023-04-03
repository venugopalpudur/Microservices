package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.Item;
import com.model.Order;
import com.repository.OrderRepository;

@Service
public class OrderServiceImpl implements OrderService{
	
	@Autowired
	private OrderRepository orderRepository;
	
	private static final String EMAIL_SERVICE = "EMAIL-SERVICE";

	@Override
	public Order addOrder(Order order) {
		Order od = orderRepository.save(order);
		if(od != null) {
			return od;
		}
		return null;
	}

	@Override
	public Order updateOrder(int orderId, Order order) {
		if(orderRepository.findById(orderId).isPresent()) {	
			Order od = orderRepository.findById(orderId).get();
			od.setItems(order.getItems());
			Order updatedOrder = orderRepository.save(od);
			if(updatedOrder != null) {
				return updatedOrder;
			}
			return null;
		}
		return null;
	}

	@Override
	public String deleteOrder(int orderId) {
		
		if(orderRepository.findById(orderId).isPresent()) {
			orderRepository.deleteById(orderId);
			return "Order of id : "+orderId+" is deleted.";
		}
		return "cannot be deleted";
	}

	@Override
	public Order searchOrder(int orderId) {
		if(orderRepository.findById(orderId).isPresent()) {
			return orderRepository.findById(orderId).get();
		}
		return null;
	}


}
