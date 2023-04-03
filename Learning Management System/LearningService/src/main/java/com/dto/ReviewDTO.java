package com.dto;

import java.time.LocalDate;

import com.model.User;

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
public class ReviewDTO {

	private int grade;

	//private User user;
	
	private String comment;
	
	private LocalDate createDate = LocalDate.now();
	
	//private LocalDate updatedDate;
}
