package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.entity.UserRole;
import com.model.User;
import com.repository.UserRepository;

import jakarta.annotation.PostConstruct;


@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients(basePackages = {"com.feignclient"})
public class RegistrationServiceApplication {
	
	/*@Autowired
	public UserRepository repository;
	
	@PostConstruct
	public void start() {
		repository.save(new User(1,"a","b", "c", "pudur.venu@gmail.com", "12345", UserRole.USER, false, false));
		
	}*/
	public static void main(String[] args) {
		SpringApplication.run(RegistrationServiceApplication.class, args);
	}

}
