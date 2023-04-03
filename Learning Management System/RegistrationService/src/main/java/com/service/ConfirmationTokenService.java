package com.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.ConfirmationToken;
import com.repository.ConfirmationTokenRepository;

import lombok.AllArgsConstructor;

@Service
public class ConfirmationTokenService {
	
	@Autowired
	private ConfirmationTokenRepository tokenRepo;
	
	public void saveConfirmationToken(ConfirmationToken confirmationToken) {
		tokenRepo.save(confirmationToken);
	}
	
	public void deleteConfirmationToken(Long id) {
		tokenRepo.deleteById(id);
	}
	
	public Optional<ConfirmationToken> findConfirmationTokenByToken(String token) {

		return tokenRepo.findConfirmationTokenByConfirmationToken(token);
	}
}
