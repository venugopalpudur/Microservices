package com.service;

import java.util.List;
import com.model.Services;

public interface ServicesService {
	
	public List<Services> getAllServices();
	
	public Services getServiceById(long id);
	
	public List<Services> getServicesByServiceName(String serviceName);
	
}