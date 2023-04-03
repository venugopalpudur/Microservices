package com.exceptions;

public class NoPaymentFoundException extends Exception {

	String message;

	public NoPaymentFoundException(String message) {
		super(message);
		this.message = message;
	}
}
