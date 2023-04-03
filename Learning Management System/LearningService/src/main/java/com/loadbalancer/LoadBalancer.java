package com.loadbalancer;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.context.annotation.Bean;

import feign.Feign;

@LoadBalancerClient("EMAIL-SERVICE")
public class LoadBalancer {

	@LoadBalanced
	@Bean
	public Feign.Builder feignBuilder(){
		return Feign.builder();
	}
}
