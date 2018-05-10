package com.bankslips.rest.enuns;

public enum RESTErrorMessages {
	CREATE_BANKSLIP_INVALID_REQUEST("Bankslip not provided in the request body"),
	CREATE_BANKSLIP_INVALID_BANKSLIP_DATA("Invalid bankslip provided.The possible reasons are:\r\n" + 
			"○ A field of the provided bankslip was null or with invalid values"),
	CANCEL_BANKSLIP_NOT_FOUND("Bankslip not found with the specified id"),
	GET_BANKSLIP_INVALID_UUID("Invalid id provided - it must be a valid UUID"),
	PAY_BANKSLIP_PAYED_STATUS_ERROR("Invalid action, this bankslip is already paied"),
	PAY_BANKSLIP_CANCELED_STATUS_ERROR("Invalid action, this bankslip is canceled");
		
	private String message;

	RESTErrorMessages(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}

		
}