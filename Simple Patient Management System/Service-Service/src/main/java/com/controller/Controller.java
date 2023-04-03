package com.controller;

import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exceptions.NoDataFoundException;
import com.exceptions.NullDataException;
import com.exceptions.PatientNotFoundException;
import com.service.ServicesService;
import com.util.Response;

import jakarta.servlet.http.HttpServletRequest;


@RestController
public class Controller {

	@Autowired
	private ServicesService services;
	
	@Autowired
	Environment environment;
	
	@GetMapping("/services")
	public ResponseEntity<?> getAllServices(HttpServletRequest request) throws NoDataFoundException {
		if(services.getAllServices() != null) {	
			Response response = new Response();
			response.setPort(environment.getProperty("local.server.port"));
			response.setServices(services.getAllServices());
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		throw new NoDataFoundException("No data found");
	}
	
	@GetMapping("/getservice/{sid}")
	public ResponseEntity<?> getServiceById(@PathVariable("sid") int sid, HttpServletRequest request) throws NoDataFoundException{
		if(services.getServiceById(sid) != null) {
			Response response = new Response();
			response.setPort(environment.getProperty("local.server.port"));
			response.setService(services.getServiceById(sid));
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		throw new NoDataFoundException("No data found");
	}
	
	@GetMapping("/getservicesbyname")
	public ResponseEntity<?> getServiceByName(@RequestParam("serviceName") String serviceName, HttpServletRequest request) throws NoDataFoundException{
		if(services.getServicesByServiceName(serviceName) != null) {
			Response response = new Response();
			response.setPort(environment.getProperty("local.server.port"));
			response.setServices(services.getServicesByServiceName(serviceName));
			return new ResponseEntity<>(response, HttpStatus.OK);
		}
		throw new NoDataFoundException("No data found");
	}
	
}





