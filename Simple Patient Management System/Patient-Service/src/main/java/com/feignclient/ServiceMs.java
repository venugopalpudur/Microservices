package com.feignclient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.model.Services;
import com.util.Response;

//value = "SERVICE-CLIENT", name="SERVICE-CLIENT",
@FeignClient(name="SERVICEMS")
public interface ServiceMs {
	
	@GetMapping("/services")
	public ResponseEntity<Response> getAllServices();
	
	@GetMapping("/getservice/{sid}")
	public ResponseEntity<Response> getServiceById(@PathVariable("sid") int sid);
	
	@GetMapping("/getservicesbyname")
	public ResponseEntity<Response> getServiceByName(@RequestParam("serviceName") String serviceName);
}
