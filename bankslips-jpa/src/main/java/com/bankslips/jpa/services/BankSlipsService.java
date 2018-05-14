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
	public List<BankSlipJPAEntity> getAll();

	/**
	 * 
	 * Get a especific bankSlip
	 * 
	 * @param id
	 * @return Optional<BankSlip>
	 */
	public Optional<BankSlipJPAEntity> findById(String id);

	/**
	 * 
	 * Persist a BankSlip
	 * 
	 * @param bankSlip
	 * @return BankSlip
	 */
	public BankSlipJPAEntity persist(BankSlipJPAEntity bankSlip);
	
	/**
	 * 
	 * Delete a BankSlip
	 * 
	 * @param bankSlip
	 * @return BankSlip
	 */
	public void delete(BankSlipJPAEntity bankSlip);
	
}
