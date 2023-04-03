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

import com.service.UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {
	
	@Autowired
	private UserService userService;
	
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
		.requestMatchers("/authenticate/**")
		.permitAll()
		.requestMatchers("/register/confirm/**")
		.permitAll()
		.requestMatchers("/register/**")
		.permitAll()
		//.requestMatchers("/get/**").hasAnyRole("ADMIN","USER") // requestMatchers(HttpMethod.POST, "/v1/post").hasAnyRole("ADMIN","USER")
		//.permitAll()
		.anyRequest()
		.authenticated().and().exceptionHandling().and()
		.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        .userDetailsService(userService);
		
		
		//.and()
		//.httpBasic();
		//.formLogin().loginPage("/sign-in").permitAll();
		
		 //http.csrf().ignoringRequestMatchers("/h2-console/**");
		 //http.headers().frameOptions().sameOrigin();
		 //http.authenticationProvider(authenticationProvider());
		return http.build();
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
