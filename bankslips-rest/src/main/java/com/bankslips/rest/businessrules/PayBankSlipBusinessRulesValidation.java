package com.bankslips.rest.businessrules;

import org.springframework.http.HttpStatus;

import com.bankslips.entities.BankSlip;
import com.bankslips.entities.StatusEnum;
import com.bankslips.rest.enuns.RESTErrorMessages;
import com.bankslips.rest.exceptions.BankSlipBusinessErrorException;

public class PayBankSlipBusinessRulesValidation {
	
	public static void validadeBankSlipPayBusinessRules(BankSlip bankSlip) throws BankSlipBusinessErrorException {
		//validating differents states to receive the payment 
		if(bankSlip.getStatus().equals(StatusEnum.CANCELED.toString())) {
			throw new BankSlipBusinessErrorException(RESTErrorMessages.PAY_BANKSLIP_CANCELED_STATUS_ERROR.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		}
		if(bankSlip.getStatus().equals(StatusEnum.PAID.toString())) {
			throw new BankSlipBusinessErrorException(RESTErrorMessages.PAY_BANKSLIP_PAIED_STATUS_ERROR.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
}
