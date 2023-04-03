package com;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.client.RestTemplate;

import com.model.Patient;
import com.model.Services;
import com.service.PatientService;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@ComponentScan(basePackages={"com.controller", "com.model", "com.repository", "com.service", "com.util", "com.exceptions"})
@EnableFeignClients(basePackages = {"com.feignclient"})
@EnableDiscoveryClient
//@ImportAutoConfiguration({FeignAutoConfiguration.class})
public class Patient_MS_Assgn_6 {

	@Autowired
	private PatientService service;
	
	@PostConstruct
	public void post() {
		service.addPatient(new Patient(1, "sdfgs", "asdgs", new HashSet<>(Arrays.asList(new Services(1, "asdgas", 125)))));
	}
	
	public static void main(String[] args) {
		SpringApplication.run(Patient_MS_Assgn_6.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
