package com.bankslips.rest.enuns;

public enum RESTSuccessMessages {
	CREATE_BANKSLIP_SUCCESS("Bankslip created"),
	CANCEL_BANKSLIP_SUCCESS("Bankslip canceled"),
	PAY_BANKSLIP_SUCCESS("Bankslip paid");
		
	private String message;
	
	RESTSuccessMessages(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}	
}
