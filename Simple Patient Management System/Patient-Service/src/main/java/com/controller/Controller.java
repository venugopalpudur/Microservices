package com.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.sound.sampled.Port;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.exceptions.NoDataFoundException;
import com.exceptions.NullDataException;
import com.exceptions.PatientNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.feignclient.ServiceMs;
import com.model.Patient;
import com.model.Services;
import com.service.PatientService;
import com.util.Response;
import com.util.Status;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.vavr.collection.Stream;
import jakarta.servlet.http.HttpServletRequest;


@RestController
@CrossOrigin("http://localhost:4200/")
public class Controller {
	
	private static final String SERVICE_M = "ServiceMs";

	@Autowired
	private PatientService service;

	@Autowired
	private ServiceMs serviceController;
	
	@GetMapping("/patient")
	public ResponseEntity<?> getPatient(HttpServletRequest request) throws NoDataFoundException {
		if(service.getAllPatients() != null) {
			Status status = new Status();
			status.setResponseStatus(service.getAllPatients() != null);
			status.setStatusCode(HttpStatus.OK);
			status.setPath(request.getRequestURI());
			status.setTimestamp(new Date());
			//status.setServices(null);
			status.setPatient(service.getAllPatients());
			return new ResponseEntity<>(status, HttpStatus.OK);
		}
		throw new NoDataFoundException("");
	}
	
	@CircuitBreaker(name = SERVICE_M, fallbackMethod = "getAllAvailableServices")
	//@Retry(name = SERVICE_M, fallbackMethod = "getAllAvailableServicess")
	@GetMapping("/services")
	public ResponseEntity<?> getAllServices(HttpServletRequest request) throws NoDataFoundException {
		
		try {
			Response resp = serviceController.getAllServices().getBody();
			List<Services>  st = resp.getServices();
			if(st != null) {			
				Status status = new Status();
				status.setResponseStatus(st != null);
				status.setStatusCode(HttpStatus.OK);
				status.setPath(request.getRequestURI());
				status.setPort("Load Balancing using instance of 'service' from = "+resp.getPort());
				status.setTimestamp(new Date());
					//status.setPatient(null); 
				status.setServices(st);
				return new ResponseEntity<>(status, HttpStatus.OK);
			}
			throw new NoDataFoundException("No data found");
		} catch (Exception e) {
			throw new NoDataFoundException("No data found");
		}
	}
	
	public ResponseEntity<?> getAllAvailableServices(Exception e){
		Status status = new Status();
		status.setResponseStatus(false);
		status.setStatusCode(HttpStatus.OK);
		status.setPath("fallBackMethodCall");
		//status.setPort("Load Balancing using instance of 'service' from = "+resp.getPort());
		status.setTimestamp(new Date());
			//status.setPatient(null); 
		status.setServices(Stream.of(new Services(1, "OPD", 0)).collect(Collectors.toList()));
		return new ResponseEntity<>(status, HttpStatus.OK);
	}
	
	@PostMapping("/addpatient")
	public ResponseEntity<?> addPatient(@RequestBody Patient pt, HttpServletRequest request) throws NullDataException{
		if (service.addPatient(pt)) {
			Status status = new Status();
			status.setResponseStatus(service.addPatient(pt));
			status.setStatusCode(HttpStatus.CREATED);
			status.setPath(request.getRequestURI());
			status.setTimestamp(new Date());
			//status.setServices(null);
			//status.setPatient(null);
			return new ResponseEntity<>(status, HttpStatus.CREATED);
		}
		throw new NullDataException("Null Data cannot be added");
	}
	
	@GetMapping("/getpatient/{pid}")
	public ResponseEntity<?> getPatientById(@PathVariable("pid") long pid, HttpServletRequest request) throws PatientNotFoundException{
		if(service.getPatientById(pid) != null) {
			Status status = new Status();
			status.setResponseStatus(service.getPatientById(pid) != null);
			status.setStatusCode(HttpStatus.OK);
			status.setPath(request.getRequestURI());
			status.setTimestamp(new Date());
			//status.setServices(null);
			status.setPatient(Arrays.asList(service.getPatientById(pid)));
			return new ResponseEntity<>(status, HttpStatus.OK);
		}
		throw new PatientNotFoundException("Patient details not found");
	}
	
	@CircuitBreaker(name = SERVICE_M, fallbackMethod = "getAllAvailableServices")
	@GetMapping("/getservice/{sid}")
	public ResponseEntity<?> getServiceById(@PathVariable("sid") int sid, HttpServletRequest request) throws NoDataFoundException{
		try {
			Response resp = serviceController.getServiceById(sid).getBody();
			Services  services = resp.getService();
			if(services != null) {
				Status status = new Status();
				status.setResponseStatus(services != null);
				status.setStatusCode(HttpStatus.OK);
				status.setPath(request.getRequestURI());
				status.setPort("Load Balancing using instance of 'service' from = "+resp.getPort());
				status.setTimestamp(new Date());
				//status.setPatient(null);
				status.setServices(Arrays.asList(services));
				return new ResponseEntity<>(status, HttpStatus.OK);
			}
			throw new NoDataFoundException("No data found");
		} catch (Exception e) {
			throw new NoDataFoundException("No data found");
		}

	}
	
	@CircuitBreaker(name = SERVICE_M, fallbackMethod = "getAllAvailableServices")
	@GetMapping("/getservicesbyname")
	public ResponseEntity<?> getServiceById(@RequestParam("serviceName") String serviceName, HttpServletRequest request) throws NoDataFoundException{
		try {
			Response resp = serviceController.getServiceByName(serviceName).getBody();
			List<Services>  services = resp.getServices();
			if(services != null) {
				Status status = new Status();
				status.setResponseStatus(services != null);
				status.setStatusCode(HttpStatus.OK);
				status.setPath(request.getRequestURI());
				status.setPort("Load Balancing using instance of 'service' from = "+resp.getPort());
				status.setTimestamp(new Date());
				//status.setPatient(null);
				status.setServices(services);
				return new ResponseEntity<>(status, HttpStatus.OK);
			}
			throw new NoDataFoundException("No data found");
		} catch (Exception e) {
			throw new NoDataFoundException("No data found");
		}
	}
	
	@PutMapping("/updatepatient/{pid}")
	public ResponseEntity<?> updatePatientById(@RequestBody Patient pt, @PathVariable("pid") long pid, HttpServletRequest request) throws PatientNotFoundException{
		if(service.updatePatient(pt, pid)) {
			Status status = new Status();
			status.setResponseStatus(service.updatePatient(pt, pid));
			status.setStatusCode(HttpStatus.OK);
			status.setPath(request.getRequestURI());
			status.setTimestamp(new Date());
			//status.setServices(null);
			//status.setPatient(null);
			return new ResponseEntity<>(status, HttpStatus.OK);
		}
		throw new PatientNotFoundException("Patient details not found");
	}
	
	@DeleteMapping("/deletepatient/{pid}")
	public ResponseEntity<?> deletePatientById(@PathVariable("pid") long pid, HttpServletRequest request) throws PatientNotFoundException{
		if(service.deletePatientById(pid)) {
			Status status = new Status();
			status.setResponseStatus(service.deletePatientById(pid));
			status.setStatusCode(HttpStatus.OK);
			status.setPath(request.getRequestURI());
			status.setTimestamp(new Date());
			//status.setServices(null);
			//status.setPatient(null);
			return new ResponseEntity<>(status, HttpStatus.OK);
		}
		throw new PatientNotFoundException("Patient details not found");
	}
	
	@DeleteMapping("/deleteallpatients")
	public ResponseEntity<?> deleteAllPatients(HttpServletRequest request) throws NoDataFoundException{
		if(service.deletePatients()) {
			Status status = new Status();
			status.setResponseStatus(service.deletePatients());
			status.setStatusCode(HttpStatus.OK);
			status.setPath(request.getRequestURI());
			status.setTimestamp(new Date());
			//status.setServices(null);
			//status.setPatient(null);
			return new ResponseEntity<>(status, HttpStatus.OK);
		}
		throw new NoDataFoundException("No data found");
	}
}





