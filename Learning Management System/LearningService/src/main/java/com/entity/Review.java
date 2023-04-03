package com.entity;

import java.sql.Date;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.model.User;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
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
@Entity
@Table(name = "review")
public class Review {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	//@Size(min = 0, max = 10, message = "Grading must be on the scale of 0-10.")
	private int grade;
	
	@JsonBackReference(value = "review")
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
	
	//@Size(min = 0, max = 50, message = "Surname must be 0-50 characters long.")
	private String comment;
	
	private LocalDate createDate = LocalDate.now();
	
	private LocalDate updatedDate;
} 



