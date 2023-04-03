package com.model;

import com.entity.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Data
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Roles")
public class Roles {
	 @Id
	 @GeneratedValue(strategy= GenerationType.IDENTITY)
	 private long id;

	 @Builder.Default
	 private UserRole userRole = UserRole.USER;

	 @Builder.Default
	 private String description = "Default User Role";
}
