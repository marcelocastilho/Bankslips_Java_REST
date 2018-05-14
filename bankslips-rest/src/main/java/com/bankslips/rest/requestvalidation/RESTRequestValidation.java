package com.bankslips.rest.requestvalidation;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import com.bankslips.entities.BankSlip;
import com.bankslips.entities.StatusEnum;
import com.bankslips.rest.enuns.RESTErrorMessages;
import com.bankslips.rest.exceptions.BankSlipsValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RESTRequestValidation {
	
	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	/**
	 * This method verify the request content of rest operation createBankSlip
	 * @param request
	 * @return BankSlip
	 * @throws BankSlipsValidationException 
	 */
	public BankSlip validateCreateBankSlipRequest(String request) throws BankSlipsValidationException {

		BankSlip bankSlip = null;

		try {
			// Try to parse the request object to java bankSlip object
			bankSlip = new ObjectMapper().readValue(request, BankSlip.class);
		} catch (Exception e) {
			// Any error in this parser must response the same message Invalid Request
			throw new BankSlipsValidationException(RESTErrorMessages.CREATE_BANKSLIP_INVALID_REQUEST.getMessage(), HttpStatus.BAD_REQUEST);
		}

		//validating the fields with null or blank
		if(StringUtils.isEmpty(bankSlip.getCustomer()) 
				|| bankSlip.getDueDate() == null 
				|| bankSlip.getTotalInCents() == 0 
				|| bankSlip.getStatus() == null ){
			throw new BankSlipsValidationException(RESTErrorMessages.CREATE_BANKSLIP_INVALID_BANKSLIP_DATA.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		}
		//validating the fields with specific values
		try {
			LocalDate.parse(bankSlip.getDueDate(),dateFormatter);
		} catch (DateTimeException e) {
			throw new BankSlipsValidationException(RESTErrorMessages.CREATE_BANKSLIP_INVALID_BANKSLIP_DATA.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		}
		if( !bankSlip.getStatus().equals(StatusEnum.PENDING.toString())) {
			throw new BankSlipsValidationException(RESTErrorMessages.CREATE_BANKSLIP_INVALID_BANKSLIP_DATA.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return bankSlip;
	}

	/**
	 * This method verify the request content of rest operation getBankSlipDetails
	 * @param request
	 * @return String
	 * @throws BankSlipsValidationException 
	 */
	public void validateGetBankSlipDetailRequest(String request) throws BankSlipsValidationException {
		Pattern p = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[34][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}");
		Matcher m = p.matcher(request);

		if(!m.matches()) {
			throw new BankSlipsValidationException(RESTErrorMessages.GET_BANKSLIP_INVALID_UUID.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
