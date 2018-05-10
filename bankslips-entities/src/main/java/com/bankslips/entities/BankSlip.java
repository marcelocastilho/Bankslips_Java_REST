package com.bankslips.entities;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_EMPTY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankSlip {
	
	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	private Optional<String> id = Optional.empty();
	@NotNull
	private String dueDate;
	@NotNull
	private long totalInCents;	
	private long fine;
	@NotNull
	private String customer;
	@NotNull
	private String status;

	public BankSlip( String dueDate, long totalInCents, String customer) {
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

	public String getDueDate() {
		return dueDate;
	}
	
	public void setDueDate(String dueDate) {
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
	
	public long getFine() {
		return fine;
	}

	public void setFine(long fine) {
		this.fine = fine;
	}
	
	public Optional<LocalDate> getDueDateDateFormat() {
	
		Optional<LocalDate> localDate = null;
		if(dueDate != null) {
			localDate = Optional.of(LocalDate.parse(dueDate,dateFormatter));
		}
		return localDate;
	}

	@Override
	public String toString() {
		return "BankSlip [id= " + id + ", dueDate= " + dueDate + ", totalInCents=" + totalInCents
				+ ", customer=" + customer + "]";
	}	
}
