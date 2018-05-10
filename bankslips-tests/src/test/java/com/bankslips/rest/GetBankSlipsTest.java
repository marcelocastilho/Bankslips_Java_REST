package com.bankslips.rest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Date;
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
	
	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final String URL_BASE = "http://localhost:8080/bankslips/";
	private static final String CUSTOMER = "Teste getAll bankSlip";
	private static final String ID_TO_CREATE = "test-find-all-aaaa-bbbb";
	private static final String ID_TO_CREATE_2 = "test-find-all-cccc-dddd";
	
	@Test
	public void GetBankSlips() throws Exception {
		//Creating two new bankSlips
		BankSlipJPAEntity bankSlipJPAEntity1 = bankSlipsService.persist(EntitiesTransformation.convertPOJOEntityToJPAEntity(createBankSlipEntity(ID_TO_CREATE, new Date(), CUSTOMER, StatusEnum.PENDING.toString(), 111000L)));
		BankSlipJPAEntity bankSlipJPAEntity2 = bankSlipsService.persist(EntitiesTransformation.convertPOJOEntityToJPAEntity(createBankSlipEntity(ID_TO_CREATE_2, new Date(), CUSTOMER, StatusEnum.PENDING.toString(), 111000L)));
		
		//calling and validating the rest method response
		mvc.perform(MockMvcRequestBuilders.get(URL_BASE)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.data.bankslip.id").value(ID_TO_CREATE));

		bankSlipsService.delete(bankSlipJPAEntity1);
		bankSlipsService.delete(bankSlipJPAEntity2);

	}
	
	private BankSlip createBankSlipEntity(String idToCancel, Date dueDate, String customer, String status, long totalInCents) {
		BankSlip BankSlip = new BankSlip();
		BankSlip.setId(Optional.of(idToCancel));
		BankSlip.setDueDate(dateFormat.format(dueDate));
		BankSlip.setCustomer(customer);
		BankSlip.setStatus(status);
		BankSlip.setTotalInCents(totalInCents);
		return BankSlip;
	}	

}
