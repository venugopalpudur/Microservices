package com.dto;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Component
@Data
@Setter
@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
public class authDTO {
	
	private String username;
	private String token; 
	
}
