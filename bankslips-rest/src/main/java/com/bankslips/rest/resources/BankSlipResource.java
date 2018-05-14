package com.bankslips.rest.resources;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.hateoas.ResourceSupport;

import com.bankslips.entities.BankSlip;
import com.bankslips.rest.BankSlipsRestControler;

public class BankSlipResource extends ResourceSupport {
	
	private final BankSlip bankSlip;
	
	public BankSlipResource(BankSlip bankSlip) {
		this.bankSlip = bankSlip;
		this.add(linkTo(methodOn(BankSlipsRestControler.class).getBankSlipDetails(bankSlip.getId().get())).withSelfRel());
	}
	
	public BankSlip getBankSlip() {
		return this.bankSlip;
	}
}