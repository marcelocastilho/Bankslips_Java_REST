package com.bankslips.entities.datatransformation;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

import com.bankslips.entities.BankSlip;
import com.bankslips.jpa.entities.BankSlipJPAEntity;

public class EntitiesTransformation {
		
	public static BankSlip convertJPAEntityToPOJOEntity(BankSlipJPAEntity jpaEntity) {
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		BankSlip bankSlipEntity = new BankSlip();
		bankSlipEntity.setDueDate(jpaEntity.getDueDate().format(dateFormatter));
		bankSlipEntity.setTotalInCents(jpaEntity.getTotalInCents());
		bankSlipEntity.setCustomer(jpaEntity.getCustomer());
		bankSlipEntity.setStatus(jpaEntity.getStatus());
		bankSlipEntity.setId(Optional.of(jpaEntity.getId()));
		return bankSlipEntity;
	}

	public static BankSlipJPAEntity convertPOJOEntityToJPAEntity(BankSlip entity) {
		BankSlipJPAEntity jpaEntity = new BankSlipJPAEntity();
		if(entity.getDueDateDateFormat().isPresent()) {
			jpaEntity.setDueDate(entity.getDueDateDateFormat().get());
		} else{
			jpaEntity.setDueDate(null);
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
