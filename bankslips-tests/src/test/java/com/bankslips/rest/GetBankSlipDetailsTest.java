package com.bankslips.rest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

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

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SprintBootStarter.class)
@AutoConfigureMockMvc
public class GetBankSlipDetailsTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private BankSlipsService bankSlipsService;

	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private static final String URL_BASE = "http://localhost:8080/bankslips/";
	private static final String CUSTOMER = "Teste getBankSlipDetails";
	
	private static final String BANKSLIP_ID = "5f41e817-0a4b-45ac-b89f-e5270b5c0688";	
	private static final String BANKSLIP_ID_2 = "5f41e817-0a4b-45ac-b89f-e5270b5c0699";
	private static final String BANKSLIP_NOT_FOUND = "5f41e817-0a4b-45ac-b89f-e5270b5c0111";	
	private static final String BANKSLIPINVALID_ID = "DC-0a4b-45ac-b89f-e5270b5c0699";
	
	
	private static final long BANKSLIP_INITIAL_VALUE = 100000;
	private static final long INTEREST_RATE_FIVE_DAYS_CORRECT  = (long) (100000*0.025);//5*0.005
	private static final long INTEREST_RATE_ELEVEN_DAYS_CORRECT = (long) (100000*0.060);//10*0.005+1*0.01
	
	@Test
	public void GetBankSlipDetailsWithFineCalcSuccess() throws Exception {

		//Creating a new bankSlips overdue 5 days
		BankSlipJPAEntity bankSlipJPAEntity1 = bankSlipsService.persist(EntitiesTransformation.convertPOJOEntityToJPAEntity(createBankSlipEntity(BANKSLIP_ID, LocalDate.now().minusDays(5), CUSTOMER, BANKSLIP_INITIAL_VALUE, StatusEnum.PENDING.toString())));
		//Creating a new bankSlips overdue 11 days
		BankSlipJPAEntity bankSlipJPAEntity2 = bankSlipsService.persist(EntitiesTransformation.convertPOJOEntityToJPAEntity(createBankSlipEntity(BANKSLIP_ID_2, LocalDate.now().minusDays(11), CUSTOMER, BANKSLIP_INITIAL_VALUE, StatusEnum.PENDING.toString())));

		//calling and validating the rest method response
		mvc.perform(MockMvcRequestBuilders.get(URL_BASE+BANKSLIP_ID)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("fine").value(INTEREST_RATE_FIVE_DAYS_CORRECT));

		//calling and validating the rest method response
		mvc.perform(MockMvcRequestBuilders.get(URL_BASE+BANKSLIP_ID_2)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("fine").value(INTEREST_RATE_ELEVEN_DAYS_CORRECT));

		bankSlipsService.delete(bankSlipJPAEntity1);
		bankSlipsService.delete(bankSlipJPAEntity2);
	}

	@Test
	public void GetBankSlipDetailsNotFoundError() throws Exception {

		//calling and validating the rest method response
		mvc.perform(MockMvcRequestBuilders.get(URL_BASE+BANKSLIP_NOT_FOUND)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound());
	}

	@Test
	public void GetBankSlipDetailsInvalidUUIDError() throws Exception {

		//calling and validating the rest method response
				mvc.perform(MockMvcRequestBuilders.get(URL_BASE+BANKSLIPINVALID_ID)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(content().string(RESTErrorMessages.GET_BANKSLIP_INVALID_UUID.getMessage()));	;
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
