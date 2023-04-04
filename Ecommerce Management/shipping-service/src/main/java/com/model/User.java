package com.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.model.DateOf;
import com.model.UserRole;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import java.util.UUID;
import java.util.stream.Collectors;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.var;

@EqualsAndHashCode(of = "uuid")
//@Data
@Setter
@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
@ToString
@Entity
@Table(name = "Users")
public class User implements UserDetails{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(length = 10)
	@NotNull(message = "Name cannot be null.")
	@Size(min = 2, max = 10, message = "Name must be 2-10 characters long.")
	private String name;
	
	@Column(length = 10)
	@Size(min = 2, max = 10, message = "Surname must be 2-10 characters long.")
	private String surname;
	
	private String username;
	
	@NotNull(message = "Email cannot be null.")
	@Email(message = "Enter a valid email address.")
	private String email;
	
	private String password;
	
	@Column(length = 13)
	private String phone;
	
	private int customerId;
	
	@Builder.Default
	private String userId = UUID.randomUUID().toString().replace("-","");		 
	
	private LocalDateTime createdDate;
	
	@OneToMany(targetEntity = DateOf.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "ud_fk", referencedColumnName = "id")
	private List<DateOf> updatedDate;
	

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(	name = "user_roles", 
				joinColumns = @JoinColumn(name = "user_id"), 
				inverseJoinColumns = @JoinColumn(name = "role_id"))
	@Builder.Default
	private List<Roles> roles = new ArrayList<>();
		
	@OneToMany(targetEntity = Notifications.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "nf_fk", referencedColumnName = "id")
	private List<Notifications> notifications;
	
	@Builder.Default
	private Boolean locked = false;
	
	@Builder.Default
	private Boolean accountExpired = false;
	
	@Builder.Default
	private Boolean credentialsExpired = false;
	
	@Builder.Default
	private Boolean enabled = false;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		roles.add(new Roles());
		List<GrantedAuthority> grantedAuthorities = roles.stream()
				.map(role-> new SimpleGrantedAuthority("ROLE_"+role.getUserRole().name().toUpperCase()))
				.collect(Collectors.toList());
		return grantedAuthorities;
	}
	
	@Override
	public String getPassword() {
		return password;
	}	

	@Override
	public String getUsername() {
		return email;
	}
	@Override
	public boolean isAccountNonExpired() {
		return !accountExpired;
	}
	@Override
	public boolean isAccountNonLocked() {
		return !locked;
	}
	@Override
	public boolean isCredentialsNonExpired() {
		return !credentialsExpired;
	}
	@Override
	public boolean isEnabled() {
		return enabled;
	}
}
