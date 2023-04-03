package com.dto;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

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
@Component
public class ErrorDTO {
	
	//@Temporal(TemporalType.TIMESTAMP)
	private LocalDateTime Time;
	private int statusCode;
	private String errorType;
	private String exception;
}
