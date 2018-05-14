package com.bankslips.entities;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import org.springframework.http.HttpStatus;

import com.bankslips.entities.datatransformation.exception.BankSlipsGenericException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * This class is the bankSlip model used as a DTO object
 * @author Marcelo Castilho
 *
 */
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
	
	/**
	 * This method return the dueDate formatted, using the mask yyyy-MM-dd.
	 * @return String
	 */
	@JsonIgnore
	public Optional<LocalDate> getDueDateDateFormat() {
	
		Optional<LocalDate> localDate = null;
		if(dueDate != null) {
			try{
				localDate = Optional.of(LocalDate.parse(dueDate,dateFormatter));
			}catch(DateTimeException e){
				throw new BankSlipsGenericException("Invalid dueDate " + e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
			}
		}
		return localDate;
	}

	@Override
	public String toString() {
		return "BankSlip [id= " + id + ", dueDate= " + dueDate + ", totalInCents=" + totalInCents
				+ ", customer=" + customer + "]";
	}	
}
