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

import com.model.ErrorDTO;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

	
	// userdefined
	@ExceptionHandler(NoPaymentFoundException.class)
	public ResponseEntity<?> handleNoDataException(NoPaymentFoundException e) {		
		ErrorDTO errorDTO = new ErrorDTO();
		errorDTO.setErrorType(NoPaymentFoundException.class.getName());
		errorDTO.setException(e.getMessage());
		return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
	}

	//nosuchelementexception
	//IncorrectResultSizeDataAccessException
	// predefined
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<?> handleMethodNotAllowedException(HttpRequestMethodNotSupportedException e, HttpServletRequest req) {
		ErrorDTO errorDTO = new ErrorDTO();
		errorDTO.setErrorType(HttpRequestMethodNotSupportedException.class.getName());
		errorDTO.setException(e.getMessage());
		return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest req) {

		ErrorDTO errorDTO = new ErrorDTO();
		errorDTO.setErrorType(HttpMessageNotReadableException.class.getName());
		errorDTO.setException(e.getMessage());
		return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);	
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException e, HttpServletRequest req) {

		ErrorDTO errorDTO = new ErrorDTO();
		errorDTO.setErrorType(MissingServletRequestParameterException.class.getName());
		errorDTO.setException(e.getMessage());
		return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
	}
	
}
