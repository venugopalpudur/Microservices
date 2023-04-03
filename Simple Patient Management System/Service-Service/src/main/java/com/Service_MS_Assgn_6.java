package com;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

import com.model.Services;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@ComponentScan(basePackages={"com.controller", "com.model", "com.repository", "com.service", "com.util", "com.exceptions"})
@EnableDiscoveryClient
public class Service_MS_Assgn_6 {
	
	public static void main(String[] args) {
		SpringApplication.run(Service_MS_Assgn_6.class, args);
	}

}
