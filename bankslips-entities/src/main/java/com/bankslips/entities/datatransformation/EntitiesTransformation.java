package com.bankslips.entities.datatransformation;

import java.util.Optional;

import com.bankslips.entities.BankSlip;
import com.bankslips.jpa.entities.BankSlipJPAEntity;

public class EntitiesTransformation {

	public static BankSlip convertJPAEntityToEntity(BankSlipJPAEntity jpaEntity) {
		BankSlip bankSlipEntity = new BankSlip();
		bankSlipEntity.setDueDate(jpaEntity.getDueDate());
		bankSlipEntity.setTotalInCents(jpaEntity.getTotalInCents());
		bankSlipEntity.setCustomer(jpaEntity.getCustomer());
		bankSlipEntity.setStatus(jpaEntity.getStatus());
		bankSlipEntity.setId(Optional.of(jpaEntity.getId()));
		return bankSlipEntity;
	}

	public static BankSlipJPAEntity convertEntityToJPAEntity(BankSlip entity) {
		BankSlipJPAEntity jpaEntity = new BankSlipJPAEntity();
		jpaEntity.setDueDate(entity.getDueDate());
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
