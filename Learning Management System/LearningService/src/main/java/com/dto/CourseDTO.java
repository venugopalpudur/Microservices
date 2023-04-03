package com.dto;

import java.time.LocalDate;
import java.util.List;

import com.entity.FileResource;
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
public class CourseDTO {

	private String title;
	
	private String description;
	
	private Integer enrollments;
	
	private FileResource fileResource;
	
	//private List<Content> content;
	
	//private int price;
}



