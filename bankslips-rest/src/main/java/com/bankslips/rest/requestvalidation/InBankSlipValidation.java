package com.bankslips.rest.requestvalidation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.bankslips.entities.BankSlip;

public class InBankSlipValidation implements ConstraintValidator<IsCorrectRequestBankSlip, BankSlip> {

	@Override
	public void initialize(IsCorrectRequestBankSlip constraintAnnotation) {

	}

	@Override
	public boolean isValid(BankSlip bankSlip, ConstraintValidatorContext context) {
		
		if (bankSlip.getDueDate() == null || bankSlip.getDueDate().isEmpty()) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("DueDate is required")
			.addPropertyNode("DueDate").addConstraintViolation();
			return false;
		}
		if (bankSlip.getCustomer() == null || bankSlip.getCustomer().isEmpty()) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Customer is required")
			.addPropertyNode("Customer").addConstraintViolation();
			return false;
		}
		if (bankSlip.getStatus() == null || bankSlip.getStatus().isEmpty()) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate("Status is required")
			.addPropertyNode("Status").addConstraintViolation();
			return false;
		}
		return true;
	}


}
