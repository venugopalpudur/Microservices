package com.service;

import java.util.List;

import org.springframework.boot.actuate.web.exchanges.HttpExchange.Principal;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.dto.PaymentDTO;
import com.dto.ResponseDTO;
import com.dto.ReviewDTO;
import com.dto.RoleDTO;
import com.entity.Course;

//@Service
public interface ActivityService {
	
	public Integer savePayment(PaymentDTO payment, String courseId, Authentication authentication);

	public Course subscribeCourse(String courseId, int payId, Authentication authentication);
	
	public String unSubscribeCourse(String courseId, Authentication authentication);
	
	public List<Course> addReviewToCourse(String courseId, ReviewDTO review, Authentication authentication);
	
	public List<Course> updateReviewToCourse(int reviewId, String courseId, ReviewDTO review, Authentication authentication);
	
	public String deleteReview(int reviewId, Authentication authentication);
	
	public String assignRoles(String userId, RoleDTO roleDTO, Authentication authentication);
	
	public String revokeRoles(String userId, RoleDTO roleDTO, Authentication authentication);
}
