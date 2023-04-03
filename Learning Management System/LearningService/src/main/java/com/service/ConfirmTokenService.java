package com.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.model.ConfirmationToken;

//@Service
public interface ConfirmTokenService {

	public void saveConfirmationToken(ConfirmationToken confirmationToken);
	
	public void deleteConfirmationToken(Long id);
	
	public Optional<ConfirmationToken> findConfirmationTokenByToken(String token);
}
