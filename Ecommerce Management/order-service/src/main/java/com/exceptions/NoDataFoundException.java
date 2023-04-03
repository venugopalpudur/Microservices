package com.exceptions;

public class NoDataFoundException extends Exception {

	String message;

	public NoDataFoundException(String message) {
		super(message);
		this.message = message;
	}
}
