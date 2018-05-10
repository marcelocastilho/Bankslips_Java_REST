package com.bankslips.rest.businessrules;

import org.springframework.http.HttpStatus;

import com.bankslips.entities.BankSlip;
import com.bankslips.entities.StatusEnum;
import com.bankslips.rest.enuns.RESTErrorMessages;
import com.bankslips.rest.exceptions.BankSlipBusinessErrorException;

public class CancelBankSlipBusinessRulesValidation {
	
	public static void validadeBankSlipCancelBusinessRules(BankSlip bankSlip) throws BankSlipBusinessErrorException {
		if(bankSlip.getStatus().equals(StatusEnum.PAID.toString())) {
			throw new BankSlipBusinessErrorException(RESTErrorMessages.PAY_BANKSLIP_PAYED_STATUS_ERROR.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
}
