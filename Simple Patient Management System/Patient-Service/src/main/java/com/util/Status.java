package com.util;

import java.security.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import javax.sound.sampled.Port;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;

import com.model.Patient;
import com.model.Services;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Component
public class Status {
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date timestamp;
	private String path;
	private String port;
	private HttpStatusCode statusCode;
	//private String tags;
	private boolean responseStatus; // true for response, else false for error or exception
	private List<Patient> patient;
	private List<Services> services;
	//private String exception;
	private Error error;
	
}
