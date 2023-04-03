package com.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class PasswordDTO {

	private String oldPassword;
	
	private String newPassword;
	
	private String confirmNewPassowrd;
}
