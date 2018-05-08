package com.bankslips.rest.enuns;

public enum RESTErrorMessages {
	CREATE_BANKSLIP_INVALID_REQUEST("Bankslip not provided in the request body"),
	CREATE_BANKSLIP_INVALID_BANKSLIP_DATA("Invalid bankslip provided.The possible reasons are:\r\n" + 
			"○ A field of the provided bankslip was null or with invalid values");

	private String message;

	RESTErrorMessages(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

		
}