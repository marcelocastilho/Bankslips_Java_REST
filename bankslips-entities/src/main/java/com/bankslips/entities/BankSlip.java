package com.bankslips.entities;

import java.util.Date;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankSlip {

	private Optional<String> id = Optional.empty();
	private Date dueDate;
	private long totalInCents;
	private String customer;
	private String status;
			
	public BankSlip( Date dueDate, long totalInCents, String customer) {
		this.dueDate = dueDate;
		this.totalInCents = totalInCents;
		this.customer = customer;
		this.status = StatusEnum.PENDING.toString();
	}
	
	public BankSlip() {	
	}
			
	public Optional<String> getId() {
		return id;
	}

	public void setId(Optional<String> id) {
		this.id = id;
	}

	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public long getTotalInCents() {
		return totalInCents;
	}
	public void setTotalInCents(long totalInCents) {
		this.totalInCents = totalInCents;
	}

	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "BankSlip [id= " + id + ", dueDate= " + dueDate + ", totalInCents=" + totalInCents
				+ ", customer=" + customer + "]";
	}	
}
