package com.model;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "Tokens")
public class ConfirmationToken {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	private String confirmationToken;
	
	private LocalDate createdDate;
	
	@OneToOne(targetEntity = User.class, fetch = FetchType.EAGER) 
	@JoinColumn(nullable = false, name="user_id")
	private User user;
	
	public ConfirmationToken(User user){
		this.user = user;
		this.createdDate = LocalDate.now();
		this.confirmationToken = UUID.randomUUID().toString();
	}
}



