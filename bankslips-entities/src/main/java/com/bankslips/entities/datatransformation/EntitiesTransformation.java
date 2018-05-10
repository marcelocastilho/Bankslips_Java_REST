package com.bankslips.entities.datatransformation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Optional;

import org.springframework.http.HttpStatus;

import com.bankslips.entities.BankSlip;
import com.bankslips.entities.datatransformation.exception.BankSlipsGenericException;
import com.bankslips.jpa.entities.BankSlipJPAEntity;

public class EntitiesTransformation {
	
	
	public static BankSlip convertJPAEntityToPOJOEntity(BankSlipJPAEntity jpaEntity) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		BankSlip bankSlipEntity = new BankSlip();
		bankSlipEntity.setDueDate(dateFormat.format(jpaEntity.getDueDate()));
		bankSlipEntity.setTotalInCents(jpaEntity.getTotalInCents());
		bankSlipEntity.setCustomer(jpaEntity.getCustomer());
		bankSlipEntity.setStatus(jpaEntity.getStatus());
		bankSlipEntity.setId(Optional.of(jpaEntity.getId()));
		return bankSlipEntity;
	}

	public static BankSlipJPAEntity convertPOJOEntityToJPAEntity(BankSlip entity) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		BankSlipJPAEntity jpaEntity = new BankSlipJPAEntity();
		try {
			jpaEntity.setDueDate(dateFormat.parse(entity.getDueDate()));
		} catch (ParseException e) {
			throw new BankSlipsGenericException("BankSlip tranformation DueDate Exception, format invalid", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		jpaEntity.setTotalInCents(entity.getTotalInCents());
		jpaEntity.setCustomer(entity.getCustomer());
		jpaEntity.setStatus(entity.getStatus());
		if(entity.getId().isPresent()) {
			jpaEntity.setId(entity.getId().get());
		}else {
			jpaEntity.setId(null);
		}
		return jpaEntity;
	}
}
