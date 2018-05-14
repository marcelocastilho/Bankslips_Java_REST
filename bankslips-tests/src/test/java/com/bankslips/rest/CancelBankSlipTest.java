package com.bankslips.rest;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.startsWith;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
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
import com.bankslips.rest.enuns.RESTSuccessMessages;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SprintBootStarter.class)
@AutoConfigureMockMvc
public class CancelBankSlipTest {

	private MockMvc mvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@MockBean
	private BankSlipRepository bankSlipRepository;

	@Before
	public void setUp() {
		this.mvc = webAppContextSetup(webApplicationContext).build();
	}

	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	private static final String URL_BASE = "http://localhost:8080/bankslips/";
	private static final String CUSTOMER = "Teste cancel bankSlip";
	private static final String ID_TO_CANCEL = "aaaa-bbbb-test-cancel-rest";
	private static final String NOT_FOUND_ID = "NOT_FOUND_ID";

	/**
	 * This test will validade if the REST method cancel is ok
	 */
	@Test	
	public void CancelBankSlipSuccess() throws Exception {

		//Creating a new bankSlip
		Optional<BankSlipJPAEntity> bankSlipJPAEntity = Optional.of(EntitiesTransformation.convertPOJOEntityToJPAEntity(createBankSlipEntity(Optional.of(ID_TO_CANCEL), LocalDate.now(), CUSTOMER, 111000L, StatusEnum.PENDING.toString())));

		BDDMockito.given(this.bankSlipRepository.findById(Mockito.anyString())).willReturn(bankSlipJPAEntity); 

		//calling and validating the rest method response
		mvc.perform(MockMvcRequestBuilders.delete(URL_BASE + ID_TO_CANCEL + "/cancel")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().string(equalTo(RESTSuccessMessages.CANCEL_BANKSLIP_SUCCESS.getMessage())));

	}

	/**
	 * This test will validate error on REST method cancelBankSlip when the bankSlip to cancel is not found
	 */
	@Test
	public void CancelBankSlipErrorNotFound() throws Exception {

		//calling and validating the rest method response
		mvc.perform(MockMvcRequestBuilders.delete(URL_BASE+NOT_FOUND_ID+"/cancel")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isNotFound())
		.andExpect(content().string(startsWith(RESTErrorMessages.CANCEL_BANKSLIP_NOT_FOUND.getMessage())));
	}

	@Test
	public void CancelBankSlipErrorInvalidStatusPayed() throws Exception {

		//Creating a new bankSlip	
		Optional<BankSlipJPAEntity> bankSlipJPAEntity = Optional.of(EntitiesTransformation.convertPOJOEntityToJPAEntity(createBankSlipEntity(Optional.of(ID_TO_CANCEL), LocalDate.now(), CUSTOMER, 111000L, StatusEnum.PAID.toString())));
		BDDMockito.given(this.bankSlipRepository.findById(Mockito.anyString())).willReturn(bankSlipJPAEntity); 

		//calling and validating the rest method response
		mvc.perform(MockMvcRequestBuilders.delete(URL_BASE+ID_TO_CANCEL+"/cancel")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isUnprocessableEntity())
		.andExpect(content().string(startsWith(RESTErrorMessages.PAY_BANKSLIP_PAYED_STATUS_ERROR.getMessage())));
	}

	private BankSlip createBankSlipEntity(Optional<String> id, LocalDate dueDate, String customer, long totalInCents, String status) {
		BankSlip BankSlip = new BankSlip();
		BankSlip.setId(id);
		BankSlip.setDueDate(dateFormatter.format(dueDate));
		BankSlip.setCustomer(customer);
		BankSlip.setTotalInCents(totalInCents);
		BankSlip.setStatus(status);
		return BankSlip;
	}
}
