package com.bankslips.entities.datatransformation.exception;

import org.springframework.http.HttpStatus;

/**
 * This exceptionClass will be used when the request is not correct 
 */

public class BankSlipsGenericException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private HttpStatus httpStatus = null;
	
	public BankSlipsGenericException(String message, HttpStatus httpStatus) {
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
