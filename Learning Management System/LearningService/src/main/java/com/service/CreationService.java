package com.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.dto.CourseDTO;
import com.entity.Course;
import com.model.User;

//@Service
public interface CreationService {
	
	public Course addCourse(CourseDTO course, Authentication authentication);
	
	public List<Course> getAllCourses();
	
	public List<Course> getCourses(String userId);
	
	public List<Course> getRegisteredCourses(String userId);
	
	public List<User> getSubscribers(String courseId, Authentication authentication);
	
	public Course updateCourse(CourseDTO courseDTO, String courseId, Authentication authentication);
	
	public String deleteCourse(String courseId, Authentication authentication);
	
	public String deleteAllCourses(Authentication authentication);
	
}
