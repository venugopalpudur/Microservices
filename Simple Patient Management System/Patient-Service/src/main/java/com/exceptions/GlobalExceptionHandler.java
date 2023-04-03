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

import com.util.Error;
import com.util.Status;

import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	// userdefined
	@ExceptionHandler(NoDataFoundException.class)
	public ResponseEntity<?> handleNoDataException(NoDataFoundException e, HttpServletRequest req) {
		Status status = new Status();
		Error err = new Error();
		err.setTitle(NoDataFoundException.class.getName());
		err.setErrorType("Database");
		err.setDetail(e.getMessage());
		status.setError(err);
		status.setPath(req.getRequestURI());
		status.setTimestamp(new Date());
		status.setStatusCode(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(status, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(NullDataException.class)
	public ResponseEntity<?> handleNullDataException(NullDataException e, HttpServletRequest req) {
		Status status = new Status();
		Error err = new Error();
		err.setTitle(NullDataException.class.getName());
		err.setErrorType("Database");
		err.setDetail(e.getMessage());
		status.setError(err);
		status.setPath(req.getRequestURI());
		status.setTimestamp(new Date());
		status.setStatusCode(HttpStatus.NOT_MODIFIED);
		return new ResponseEntity<>(status, HttpStatus.NOT_MODIFIED);
	}

	@ExceptionHandler(PatientNotFoundException.class)
	public ResponseEntity<?> handleAccountNotFoundException(PatientNotFoundException e, HttpServletRequest req) {
		Status status = new Status();
		Error err = new Error();
		err.setTitle(PatientNotFoundException.class.getName());
		err.setErrorType("Database");
		err.setDetail(e.getMessage());
		status.setError(err);
		status.setPath(req.getRequestURI());
		status.setTimestamp(new Date());
		status.setStatusCode(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(status, HttpStatus.NOT_FOUND);
	}

	
	
	// predefined
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<?> handleMethodNotAllowedException(HttpRequestMethodNotSupportedException e, HttpServletRequest req) {
		Status status = new Status();
		Error err = new Error();
		err.setTitle(HttpRequestMethodNotSupportedException.class.getName());
		err.setErrorType("Client");
		err.setDetail(e.getMessage());
		status.setError(err);
		status.setPath(req.getRequestURI());
		status.setTimestamp(new Date());
		status.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED);
		return new ResponseEntity<>(status, HttpStatus.METHOD_NOT_ALLOWED);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest req) {
		Status status = new Status();
		Error err = new Error();
		err.setTitle(HttpMessageNotReadableException.class.getName());
		err.setErrorType("Client");
		err.setDetail("Request Body cannot be empty");
		status.setError(err);
		status.setPath(req.getRequestURI());
		status.setTimestamp(new Date());
		status.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED);
		return new ResponseEntity<>(status, HttpStatus.METHOD_NOT_ALLOWED);
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest req) {
		Status status = new Status();
		Error err = new Error();
		err.setTitle(MissingServletRequestParameterException.class.getName());
		err.setErrorType("Client");
		err.setDetail("Request Parameters cannot be empty");
		status.setError(err);
		status.setPath(req.getRequestURI());
		status.setTimestamp(new Date());
		status.setStatusCode(HttpStatus.METHOD_NOT_ALLOWED);
		return new ResponseEntity<>(status, HttpStatus.METHOD_NOT_ALLOWED);
	}
	
	@ExceptionHandler(FeignException.class)
	public ResponseEntity<?> handleFeignException(FeignException e){
		return new ResponseEntity<>("Feign Exception", HttpStatus.OK);
	}
}
