package com.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.service.impls.AccountServiceImpl;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
	
	@Autowired
	private AccountServiceImpl userService;
	
	@Autowired
	private JwtAuthFilter jwtAuthFilter;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder; 
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		http.csrf().disable().cors().disable(); 
		http.authorizeHttpRequests()
		.requestMatchers(HttpMethod.OPTIONS, "/**")
		.permitAll()
		.requestMatchers("/api/v1/auth/authenticate/**")
		.permitAll()
		.requestMatchers("/api/v1/auth/register/confirm/**")
		.permitAll()
		.requestMatchers("/api/v1/auth/confirm/e-mail/**")
		.permitAll()
		.requestMatchers("/api/v1/auth/register/**")
		.permitAll()
		.requestMatchers("/api/v1/course/getall/**")
		.permitAll()
		.requestMatchers("/actuator/**")
		.permitAll()
		.anyRequest()
		.authenticated().and().exceptionHandling().and()
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .userDetailsService(userService);
		 
		return http.build();
	}
	
	@Bean
	public WebSecurityCustomizer webSecurityCustomizer() {
	    return (web) -> web.ignoring().requestMatchers("/h2-console/**");
	}
	
	/*@Bean
	  public DaoAuthenticationProvider authenticationProvider() {
	      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
	       
	      authProvider.setUserDetailsService(userService);
	      authProvider.setPasswordEncoder(bCryptPasswordEncoder);
	   
	      return authProvider;
	  }*/
	
	@Bean
	public static BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	/*@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService)
				.passwordEncoder(bCryptPasswordEncoder());
	}*/
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
	           throws Exception {
	    return authenticationConfiguration.getAuthenticationManager();
	}	
	
}
