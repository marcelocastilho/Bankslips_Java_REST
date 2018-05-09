package com.bankslips.rest.exceptions;

import org.springframework.http.HttpStatus;

/**
 * This exceptionClass will be used when the request is not correct 
 */

public class BankSlipsValidationException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private HttpStatus httpStatus = null;
	
	public BankSlipsValidationException(String message, HttpStatus httpStatus) {
		super(message);
		this.httpStatus = httpStatus;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}	
}
