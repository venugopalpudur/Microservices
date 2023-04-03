package com.exceptions;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.funcs.Funcs;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	@Autowired
	private Funcs funcs;
	
	// userdefined
	@ExceptionHandler(NoDataFoundException.class)
	public ResponseEntity<?> handleNoDataException(NoDataFoundException e) {		
		return funcs.generateErrorResponse(NoDataFoundException.class.getName(), HttpStatus.NO_CONTENT, e.getMessage());
	}

	@ExceptionHandler(NullDataException.class)
	public ResponseEntity<?> handleNullDataException(NullDataException e, HttpServletRequest req) {
		return funcs.generateErrorResponse(NullDataException.class.getName(), HttpStatus.NO_CONTENT, e.getMessage());
	}
	
	// predefined
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<?> handleMethodNotAllowedException(HttpRequestMethodNotSupportedException e, HttpServletRequest req) {

		return funcs.generateErrorResponse(HttpRequestMethodNotSupportedException.class.getName(), HttpStatus.METHOD_NOT_ALLOWED, e.getMessage());
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException e, HttpServletRequest req) {

		return funcs.generateErrorResponse(EntityNotFoundException.class.getName(), HttpStatus.NO_CONTENT, e.getMessage());
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest req) {

		return funcs.generateErrorResponse(HttpMessageNotReadableException.class.getName(), HttpStatus.METHOD_NOT_ALLOWED, e.getMessage());
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest req) {

		return funcs.generateErrorResponse(MissingServletRequestParameterException.class.getName(), HttpStatus.METHOD_NOT_ALLOWED, e.getMessage());
	}
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException e, HttpServletRequest req) {

		return funcs.generateErrorResponse(UsernameNotFoundException.class.getName(), HttpStatus.NON_AUTHORITATIVE_INFORMATION, e.getMessage());
	}
	
	@ExceptionHandler(DisabledException.class)
	public ResponseEntity<?> handleDisabledException(DisabledException e, HttpServletRequest req) {

		return funcs.generateErrorResponse(DisabledException.class.getName(), HttpStatus.OK, e.getMessage());
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleException(Exception e, HttpServletRequest req) {

		return funcs.generateErrorResponse(Exception.class.getName(), HttpStatus.NON_AUTHORITATIVE_INFORMATION, e.getMessage());
	}
}
