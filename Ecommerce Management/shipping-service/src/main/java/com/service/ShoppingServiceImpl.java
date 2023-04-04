package com.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dto.CartDto;
import com.dto.CustomerDto;
import com.dto.InventoryDto;
import com.dto.ItemDto;
import com.dto.OrderDto;
import com.dto.ProductDto;
import com.dto.ResponseDto;
import com.feignclient.CartService;
import com.feignclient.CustomerService;
import com.feignclient.InventoryService;
import com.feignclient.OrderService;
import com.feignclient.ProductService;
import com.model.Cart;
import com.model.Customer;
import com.model.CustomerCart;
import com.model.CustomerOrder;
import com.model.Inventory;
import com.model.InventoryProduct;
import com.model.Item;
import com.model.Order;
import com.model.Product;
import com.repository.CustomerCartRepository;
import com.repository.CustomerOrderRepository;
import com.repository.InventoryProductRepository;

@Service
public class ShoppingServiceImpl implements ShoppingService {

	@Autowired
	private ProductService productService;

	@Autowired
	private InventoryService inventoryService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CartService cartService;

	@Autowired
	private CustomerCartRepository customerCartRepository;

	@Autowired
	private InventoryProductRepository inventoryProductRepository;

	@Autowired
	private CustomerOrderRepository customerOrderRepository;

	@Override
	public ResponseDto createProduct(ProductDto product) {
		Product pro = productService.addProduct(product).getBody();

		ResponseDto response = new ResponseDto();
		if (pro != null) {
			InventoryDto inventory = new InventoryDto();
			inventory.setProductId(pro.getId());
			inventory.setQuantity(pro.getQuantity());

			Inventory inv = inventoryService.addInventory(inventory).getBody();
			InventoryProduct inventoryProduct = new InventoryProduct();
			inventoryProduct.setInventoryId(inv.getId());
			inventoryProduct.setProductId(pro.getId());
			InventoryProduct invPro = inventoryProductRepository.save(inventoryProduct);
			response.setProduct(pro);
			response.setInventory(inv);
			response.setInventoryProduct(invPro);
		}
		return response;
	}

	@Override
	public ResponseDto createCustomer(CustomerDto customer) {
		Customer cust = customerService.addCustomer(customer).getBody();
		ResponseDto response = new ResponseDto();
		if (cust != null) {
			CartDto cart = new CartDto();
			cart.setItems(null);
			Cart ca = cartService.addCart(cart).getBody();

			if (cust != null && ca != null) {
				CustomerCart customerCart = new CustomerCart();
				customerCart.setCartId(ca.getId());
				customerCart.setCustomerId(cust.getId());

				response.setCustomer(cust);
				response.setCart(ca);
				response.setCustomerCart(customerCartRepository.save(customerCart));
			}
		}
		return response;
	}

	@Override
	public ResponseDto addProductsToCart(int customerId, CartDto cart) {
		Customer customer = customerService.searchCustomer(customerId).getBody();
		ResponseDto response = new ResponseDto();
		CustomerCart customerCart = customerCartRepository.findByCustomerId(customerId); // fault
		if(customerCart != null) {
			Cart cartUpdate = cartService.searchCart(customerCart.getCartId()).getBody();
			List<Item> listItems = new ArrayList<>();
			for(ItemDto it : cart.getItems()) {
				Item item = new Item();
				item.setItemId(UUID.randomUUID().toString().replace("-",""));
				item.setPrice(productService.searchProduct(it.getProductId()).getBody().getPrice());
				item.setProductName(it.getProductName());
				item.setQuantity(it.getQuantity());
				item.setProductId(it.getProductId());
				listItems.add(item);
			}
			cartUpdate.setItems(listItems);
			Cart ca = cartService.updateCart(customerCart.getCartId(), cartUpdate).getBody();
			response.setCustomer(customer);
			response.setCustomerCart(customerCart);
			response.setCart(ca);
		}
		return response;
	}

	@Override
	public ResponseDto placeOrder(int customerId) {
		Customer customer = customerService.searchCustomer(customerId).getBody();
		ResponseDto response = new ResponseDto();
		CustomerCart customerCart = customerCartRepository.findByCustomerId(customerId);
		
		if (customerCart != null) {
			Cart ca = cartService.searchCart(customerCart.getCartId()).getBody();
			OrderDto ord = new OrderDto();
			
			List<Item> listItems = new ArrayList<>();
			for(Item it : ca.getItems()) {
				it.setId(null);
				listItems.add(it);
			}
			
			ord.setItems(listItems);
			Order order = null;
			
			for (Item item : ca.getItems()) {
				InventoryProduct invProduct = inventoryProductRepository
						.findByProductId(item.getProductId());
				Inventory invs = inventoryService.searchInventory(invProduct.getInventoryId())
						.getBody();
				System.out.println(invs.getQuantity());
				if(invs.getQuantity() > 0) {
					order = orderService.addOrder(ord).getBody();
				}else {
					order = null;
				}
			}
			
		
			if (order != null) {
				Cart cartDto = cartService.searchCart(customerCart.getCartId()).getBody();
				cartDto.setItems(null);
				Cart cartEmpty = cartService.updateCart(customerCart.getCartId(), cartDto).getBody();
				response.setCart(cartEmpty);
				if (cartEmpty != null) {
					for (Item item : ca.getItems()) {
						InventoryProduct inventoryProduct = inventoryProductRepository
								.findByProductId(item.getProductId());
						Inventory updateInv = inventoryService.searchInventory(inventoryProduct.getInventoryId())
								.getBody();
						updateInv.setQuantity(updateInv.getQuantity() - item.getQuantity());
						Inventory inv = inventoryService.updateInventory(inventoryProduct.getInventoryId(), updateInv)
								.getBody();
						response.setInventory(inv);
						
					}
					CustomerOrder customerOrder = new CustomerOrder();
					customerOrder.setCustomerId(customerId);
					customerOrder.setOrderId(order.getId());
					CustomerOrder custOd = customerOrderRepository.save(customerOrder);
					response.setCustomerOrder(custOd);
					response.setOrder(order);
				}
			}
			response.setCustomerCart(customerCart);
			response.setCustomer(customer);
			
		}
		return response;
	}

	@Override
	public ResponseDto viewAllOrders(int customerId) {
		ResponseDto response = new ResponseDto();
		List<Order> allOrders = new ArrayList<>();
		response.setCustomer(customerService.searchCustomer(customerId).getBody());
		List<CustomerOrder> customerOrder = customerOrderRepository.findByCustomerId(customerId);
		response.setViewCustomerOrders(customerOrder);
		response.setCustomerCart(customerCartRepository.findByCustomerId(customerId));
		
		response.setCart(cartService.searchCart(response.getCustomerCart().getCartId()).getBody());
		for(CustomerOrder cOrder : customerOrder) {
			allOrders.add(orderService.searchOrder(cOrder.getOrderId()).getBody());
		}
		response.setViewAllOrders(allOrders);
		return response;
	}

}
