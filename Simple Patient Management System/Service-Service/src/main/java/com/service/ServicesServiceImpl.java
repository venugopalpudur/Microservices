package com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.model.Services;
import com.repository.ServiceRepository;

@org.springframework.stereotype.Service
public class ServicesServiceImpl implements ServicesService{

	@Autowired
	private ServiceRepository repo;

	@Override
	public List<Services> getAllServices() {
		if(repo.findAll() != null) {
			return repo.findAll();
		}
		return null;
	}

	@Override
	public Services getServiceById(long id) {
		if(repo.findById(id).isPresent()) {
			return (Services)repo.findById(id).get();
		}
		return null;
	}

	@Override
	public List<Services> getServicesByServiceName(String serviceName) {
		if(repo.findByServiceName(serviceName) != null) {
			return repo.findByServiceName(serviceName);
		}
		return null;
	}
}
