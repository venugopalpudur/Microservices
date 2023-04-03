package com.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode(of = "uuid")
@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Payment")
public class Payment {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Builder.Default
	private String paymentId = UUID.randomUUID().toString().replace("-","");
	
	private String userId;
	
	private String email;
	
	private String courseId;
	
	private String amount;
	
	private String remarks;
	
	private int otp; 
	
	@Builder.Default
	private Boolean isConfirm = false;

}
