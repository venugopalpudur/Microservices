package com.exceptions;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	
	// userdefined
	@ExceptionHandler(NoDataFoundException.class)
	public ResponseEntity<?> handleNoDataException(NoDataFoundException e) {		
		return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
	}

	@ExceptionHandler(NullDataException.class)
	public ResponseEntity<?> handleNullDataException(NullDataException e, HttpServletRequest req) {
		return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
	}
	
	// predefined
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<?> handleMethodNotAllowedException(HttpRequestMethodNotSupportedException e, HttpServletRequest req) {

		return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
	}
	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException e, HttpServletRequest req) {

		return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest req) {

		return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest req) {

		return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
	}
	
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleException(Exception e, HttpServletRequest req) {

		return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
	}
}
