package com.bankslips.jpa.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.bankslips.jpa.entities.BankSlipJPAEntity;

@Service
public interface BankSlipsService {

	/**
	 * 
	 * Get all the bankSlips
	 * 
	 * @return List<BankSlip>
	 */
	List<BankSlipJPAEntity> getAll();

	/**
	 * 
	 * Get a especific bankSlip
	 * 
	 * @param id
	 * @return Optional<BankSlip>
	 */
	Optional<BankSlipJPAEntity> findById(String id);

	/**
	 * 
	 * Persist a BankSlip
	 * 
	 * @param bankSlip
	 * @return BankSlip
	 */
	BankSlipJPAEntity persist(BankSlipJPAEntity bankSlip);
	
}
