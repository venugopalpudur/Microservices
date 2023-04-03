package com.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.entity.Course;
import com.entity.DateOf;
import com.entity.Notifications;
import com.entity.Review;
import com.model.Roles;
import com.model.User;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Setter
@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString 
@Component
public class loginDTO {

	private String username;
	private String password;	
	
}
