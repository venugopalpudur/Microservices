package com.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.Product;
import com.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService{
	
	@Autowired
	private ProductRepository productRepository;
	
	private static final String EMAIL_SERVICE = "EMAIL-SERVICE";

	@Override
	public Product addProduct(Product product) {
		Product pro = productRepository.save(product);
		if(pro != null) {
			return pro;
		}
		return null;
	}

	@Override
	public Product updateProduct(int productId, Product product) {
		if(productRepository.findById(productId).isPresent()) {
			
			Product pro = productRepository.findById(productId).get();
			pro.setProductName(product.getProductName());
			pro.setProductDescription(product.getProductDescription());
			pro.setPrice(product.getPrice());
			pro.setQuantity(product.getQuantity());
			Product updatedProduct = productRepository.save(pro);
			if(updatedProduct != null) {
				return updatedProduct;
			}
			return null;
		}
		return null;
	}

	@Override
	public String deleteProduct(int productId) {
		
		if(productRepository.findById(productId).isPresent()) {
			productRepository.deleteById(productId);
			return "customer of id : "+productId+" is deleted.";
		}
		return "cannot be deleted";
	}

	@Override
	public Product searchProduct(int productId) {
		if(productRepository.findById(productId).isPresent()) {
			return productRepository.findById(productId).get();
		}
		return null;
	}


}
