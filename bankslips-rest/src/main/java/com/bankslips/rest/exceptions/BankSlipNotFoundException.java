package com.bankslips.rest.exceptions;

import org.springframework.http.HttpStatus;

public class BankSlipNotFoundException extends RuntimeException {
		
	private static final long serialVersionUID = 1L;
	
	HttpStatus httpStatus = null;

	public BankSlipNotFoundException(String errorMessage, HttpStatus httpStatus) {
		super(errorMessage);
		this.httpStatus = httpStatus;	
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}
	
	
}
