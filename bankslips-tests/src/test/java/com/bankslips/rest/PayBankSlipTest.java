package com.bankslips.rest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.bankslips.entities.BankSlip;
import com.bankslips.entities.StatusEnum;
import com.bankslips.entities.datatransformation.EntitiesTransformation;
import com.bankslips.jpa.entities.BankSlipJPAEntity;
import com.bankslips.jpa.services.BankSlipsService;
import com.bankslips.main.SprintBootStarter;
import com.bankslips.rest.enuns.RESTErrorMessages;
import com.bankslips.rest.enuns.RESTSuccessMessages;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SprintBootStarter.class)
@AutoConfigureMockMvc
public class PayBankSlipTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private BankSlipsService bankSlipsService;

	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private static final String URL_BASE = "http://localhost:8080/bankslips/";
	private static final String CUSTOMER = "Teste pay bankSlip";
	private static final String ID_TO_PAY = "aaaa-bbbb-test-pay-rest";
	private static final String NOT_FOUND_ID = "NOT_FOUND_ID";

	/**
	 * This test will validade if the REST method cancel is ok
	 */
	@Test	
	public void PayBankSlipSuccess() throws Exception {

		//Creating a new bankSlip
		BankSlipJPAEntity bankSlipJPAEntity = bankSlipsService.persist(EntitiesTransformation.convertPOJOEntityToJPAEntity(createBankSlipEntity(ID_TO_PAY, LocalDate.now(), CUSTOMER,111000L, StatusEnum.PENDING.toString())));

		//calling and validating the rest method response
		mvc.perform(MockMvcRequestBuilders.put(URL_BASE+ID_TO_PAY+"/pay")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().string(equalTo(RESTSuccessMessages.PAY_BANKSLIP_SUCCESS.getMessage())));

		bankSlipsService.delete(bankSlipJPAEntity);
	}

	/**
	 * This test will validate error on REST method cancelBankSlip when the bankSlip to cancel is not found
	 */
	@Test
	public void PayBankSlipErrorNotFound() throws Exception {

		//calling and validating the rest method response
		mvc.perform(MockMvcRequestBuilders.put(URL_BASE+NOT_FOUND_ID+"/pay")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound())
		.andExpect(content().string(startsWith(RESTErrorMessages.CANCEL_BANKSLIP_NOT_FOUND.getMessage())));
	}

	@Test
	public void PayBankSlipErrorInvalidStatusPayed() throws Exception {

		//Creating a new bankSlip to cancel
		BankSlipJPAEntity bankSlipJPAEntity = bankSlipsService.persist(EntitiesTransformation.convertPOJOEntityToJPAEntity(createBankSlipEntity(ID_TO_PAY, LocalDate.now(), CUSTOMER,111000L, StatusEnum.PENDING.toString())));
		bankSlipJPAEntity.setStatus(StatusEnum.PAID.toString());
		bankSlipsService.persist(bankSlipJPAEntity);
		
		//calling and validating the rest method response
		mvc.perform(MockMvcRequestBuilders.put(URL_BASE+ID_TO_PAY+"/pay")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isUnprocessableEntity())
		.andExpect(content().string(startsWith(RESTErrorMessages.PAY_BANKSLIP_PAYED_STATUS_ERROR.getMessage())));
	}
	
	@Test
	public void PayBankSlipErrorInvalidStatusCanceled() throws Exception {

		//Creating a new bankSlip to cancel
		BankSlipJPAEntity bankSlipJPAEntity = bankSlipsService.persist(EntitiesTransformation.convertPOJOEntityToJPAEntity(createBankSlipEntity(ID_TO_PAY, LocalDate.now(), CUSTOMER,111000L, StatusEnum.PENDING.toString())));
		bankSlipJPAEntity.setStatus(StatusEnum.CANCELED.toString());
		bankSlipsService.persist(bankSlipJPAEntity);
		
		//calling and validating the rest method response
		mvc.perform(MockMvcRequestBuilders.put(URL_BASE+ID_TO_PAY+"/pay")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isUnprocessableEntity())
		.andExpect(content().string(startsWith(RESTErrorMessages.PAY_BANKSLIP_CANCELED_STATUS_ERROR.getMessage())));
	}
	
	private BankSlip createBankSlipEntity(String id, LocalDate dueDate, String customer, long totalInCents, String status) {
		BankSlip BankSlip = new BankSlip();
		BankSlip.setId(Optional.of(id));
		BankSlip.setDueDate(dateFormatter.format(dueDate));
		BankSlip.setCustomer(customer);
		BankSlip.setTotalInCents(totalInCents);
		BankSlip.setStatus(status);
		return BankSlip;
	}
}
