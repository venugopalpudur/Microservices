package com.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PatientNotFoundException extends Exception {

	String msg;

	public PatientNotFoundException(String msg) {
		super(msg);
	}

}
