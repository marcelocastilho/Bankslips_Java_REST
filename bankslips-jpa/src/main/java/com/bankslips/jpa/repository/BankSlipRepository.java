package com.bankslips.jpa.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.bankslips.jpa.entities.BankSlipJPAEntity;

public interface BankSlipRepository extends JpaRepository<BankSlipJPAEntity, String>{

}
