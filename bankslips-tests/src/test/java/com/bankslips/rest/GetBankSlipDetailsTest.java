package com.bankslips.rest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.bankslips.entities.BankSlip;
import com.bankslips.entities.StatusEnum;
import com.bankslips.entities.datatransformation.EntitiesTransformation;
import com.bankslips.jpa.entities.BankSlipJPAEntity;
import com.bankslips.jpa.repository.BankSlipRepository;
import com.bankslips.main.SprintBootStarter;
import com.bankslips.rest.enuns.RESTErrorMessages;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SprintBootStarter.class)
@WebAppConfiguration
public class GetBankSlipDetailsTest {
	
	private MockMvc mvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;

	@MockBean
	private BankSlipRepository bankSlipRepository;

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
	
	@Before
	public void setUp() {
		this.mvc = webAppContextSetup(webApplicationContext).build();
	}
	
	@Test
	@WithMockUser(username="admin")
	public void GetBankSlipDetailsWithFineCalcSuccess() throws Exception {
		
		//Creating database bankSlips overdue 5 days
		Optional<BankSlipJPAEntity> entitie = Optional.of(EntitiesTransformation.convertPOJOEntityToJPAEntity(createBankSlipEntity(BANKSLIP_ID, LocalDate.now().minusDays(5), CUSTOMER, BANKSLIP_INITIAL_VALUE, StatusEnum.PENDING.toString())));
		
		BDDMockito.given(this.bankSlipRepository.findById(BANKSLIP_ID)).willReturn(entitie);
		//Creating database bankSlips overdue 11 days
		BDDMockito.given(this.bankSlipRepository.findById(BANKSLIP_ID_2)).willReturn(Optional.of(EntitiesTransformation.convertPOJOEntityToJPAEntity(createBankSlipEntity(BANKSLIP_ID_2, LocalDate.now().minusDays(11), CUSTOMER, BANKSLIP_INITIAL_VALUE, StatusEnum.PENDING.toString()))));
		
		//calling and validating the rest method response
		mvc.perform(MockMvcRequestBuilders.get(URL_BASE+BANKSLIP_ID)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.bankSlip.fine").value(INTEREST_RATE_FIVE_DAYS_CORRECT));

		//calling and validating the rest method response
		mvc.perform(MockMvcRequestBuilders.get(URL_BASE+BANKSLIP_ID_2)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.bankSlip.fine").value(INTEREST_RATE_ELEVEN_DAYS_CORRECT));

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
