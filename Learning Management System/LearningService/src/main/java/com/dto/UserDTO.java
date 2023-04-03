package com.dto;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.model.Roles;

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
public class UserDTO {

	private String name;
	private String surname;
	private String username;
	private String email;
	private String phone;
	private String password;
	private List<Roles> roles;
}
