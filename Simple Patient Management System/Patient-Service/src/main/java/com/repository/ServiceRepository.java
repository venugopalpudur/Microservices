package com.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.model.Services;

@Repository
public interface ServiceRepository extends JpaRepository<Services, Long>{

	public List<Services> findByServiceName(String serviceName);
}
