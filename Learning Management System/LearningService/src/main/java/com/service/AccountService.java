package com.service;

import org.springframework.stereotype.Service;

//@Service
public interface AccountService {

	public String accountLocked(String userId);
	
	public String accountExpired();
	
	public String credentialsExpired();
}
