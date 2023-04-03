package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.Image;
import com.model.Product;

import jakarta.transaction.Transactional;


@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
	
	@Transactional
	public Image findByProductId(int productId);
}
