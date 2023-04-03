package com.dto;

import java.util.List;

import com.model.Roles;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PaymentDTO {

	private String userId;
	
	private String email;
	
	private String courseId;
	
	private String amount;
	
	private String remarks;
	
}
