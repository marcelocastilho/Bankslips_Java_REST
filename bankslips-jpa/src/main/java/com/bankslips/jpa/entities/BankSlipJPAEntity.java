package com.bankslips.jpa.entities;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * This class is the bankSlip model for the JPA
 * @author Marcelo Castilho
 *
 */
@Entity
@Table(name = "bankslips")
public class BankSlipJPAEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
	@Column(name = "id", unique = true)
	private String id;

	@Column(name = "due_date", nullable = false)
	private LocalDate dueDate;

	@Column(name = "total_in_cents", nullable = false)
	private long totalInCents;

	@Column(name = "customer", nullable = false)
	private String customer;

	@Column(name = "status", nullable = false)
	private String status;

	public BankSlipJPAEntity( LocalDate dueDate, long totalInCents, String customer, String status) {
		this.dueDate = dueDate;
		this.totalInCents = totalInCents;
		this.customer = customer;
		this.status = status;
	}

	public BankSlipJPAEntity( ) {	
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public LocalDate getDueDate() {
		return dueDate;
	}
	public void setDueDate(LocalDate dueDate) {
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
