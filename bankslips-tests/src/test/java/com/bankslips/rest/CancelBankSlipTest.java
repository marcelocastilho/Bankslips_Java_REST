package com.bankslips.rest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.SimpleDateFormat;
import java.util.Date;
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
public class CancelBankSlipTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private BankSlipsService bankSlipsService;

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private static final String URL_BASE = "http://localhost:8080/bankslips/";
	private static final String CUSTOMER = "Teste cancel bankSlip";
	private static final String ID_TO_CANCEL = "aaaa-bbbb-test-cancel-rest";

	/**
	 * This test will validade if the REST method cancel is ok
	 */
	@Test	
	public void CancelBankSlipSuccess() throws Exception {

		//Creating a new bankSlip
		String id = ID_TO_CANCEL;
		BankSlipJPAEntity bankSlipJPAEntity = bankSlipsService.persist(EntitiesTransformation.convertPOJOEntityToJPAEntity(createBankSlipEntity(ID_TO_CANCEL, new Date(), CUSTOMER, StatusEnum.PENDING.toString(), 111000L)));

		//calling and validating the rest method response
		mvc.perform(MockMvcRequestBuilders.delete(URL_BASE+id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(id))
		.andExpect(status().isOk())
		.andExpect(content().string(equalTo(RESTSuccessMessages.CANCEL_BANKSLIP_SUCCESS.getMessage())));
				
		bankSlipsService.delete(bankSlipJPAEntity);
	}

	/**
	 * This test will validate error on REST method cancelBankSlip when the bankSlip to cancel is not found
	 */
	@Test
	public void CancelBankSlipErrorNotFound() throws Exception {
		//Creating a id that does not exists
		String id = ID_TO_CANCEL;

		//calling and validating the rest method response
		mvc.perform(MockMvcRequestBuilders.delete(URL_BASE+id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(id))
		.andExpect(status().isNotFound())
		.andExpect(content().string(startsWith(RESTErrorMessages.CANCEL_BANKSLIP_NOT_FOUND.getMessage())));
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
