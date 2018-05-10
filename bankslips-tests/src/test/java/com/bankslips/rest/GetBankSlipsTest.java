package com.bankslips.rest;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SprintBootStarter.class)
@AutoConfigureMockMvc
public class GetBankSlipsTest {

	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private BankSlipsService bankSlipsService;
	
	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private static final String URL_BASE = "http://localhost:8080/bankslips/";
	private static final String CUSTOMER = "Teste getAll bankSlip";
	private static final String ID_TO_CREATE = "test-find-all-aaaa-bbbb";
	private static final String ID_TO_CREATE_2 = "test-find-all-cccc-dddd";
	
	@Test
	public void GetBankSlips() throws Exception {
		//Creating two new bankSlips
		BankSlipJPAEntity bankSlipJPAEntity1 = bankSlipsService.persist(EntitiesTransformation.convertPOJOEntityToJPAEntity(createBankSlipEntity(ID_TO_CREATE, LocalDate.now(), CUSTOMER, 111000L, StatusEnum.PENDING.toString())));
		BankSlipJPAEntity bankSlipJPAEntity2 = bankSlipsService.persist(EntitiesTransformation.convertPOJOEntityToJPAEntity(createBankSlipEntity(ID_TO_CREATE_2, LocalDate.now(), CUSTOMER, 111000L, StatusEnum.PENDING.toString())));
		
		//calling and validating the rest method response
		mvc.perform(MockMvcRequestBuilders.get(URL_BASE)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().string(containsString(ID_TO_CREATE)))
		.andExpect(content().string(containsString(ID_TO_CREATE_2)));
		
		bankSlipsService.delete(bankSlipJPAEntity1);
		bankSlipsService.delete(bankSlipJPAEntity2);

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
