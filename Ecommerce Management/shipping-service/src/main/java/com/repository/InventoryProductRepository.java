package com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.CustomerCart;
import com.model.InventoryProduct;
import com.model.Product;

import jakarta.transaction.Transactional;


@Repository
public interface InventoryProductRepository extends JpaRepository<InventoryProduct, Integer> {

	@Transactional
	public InventoryProduct findByProductId(int productId);
}
