package com.dto;

import com.model.UserRole;

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
public class RoleDTO {

	 private UserRole userRole;

	 //@Size(min = 0, max = 50, message = "Role description must be 0-50 characters long.")
	 private String description;
}
