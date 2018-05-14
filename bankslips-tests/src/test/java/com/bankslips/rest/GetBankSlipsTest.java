package com.bankslips.rest;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
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

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SprintBootStarter.class)
@AutoConfigureMockMvc
public class GetBankSlipsTest {

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
	private static final String CUSTOMER = "Teste getAll bankSlip";
	private static final String ID_TO_CREATE = "test-find-all-aaaa-bbbb";
	private static final String ID_TO_CREATE_2 = "test-find-all-cccc-dddd";

	@Test
	public void GetBankSlips() throws Exception {
				
		//Creating a list of bankslip
		BDDMockito.given(this.bankSlipRepository.findAll()).willReturn(getListOfBankSlipJPAEntity());

		//calling and validating the rest method response
		mvc.perform(MockMvcRequestBuilders.get(URL_BASE)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().string(containsString(ID_TO_CREATE)))
		.andExpect(content().string(containsString(ID_TO_CREATE_2)));

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
	
	private List<BankSlipJPAEntity> getListOfBankSlipJPAEntity() {
		List<BankSlipJPAEntity> listOfBankSlipJPAEntity = new ArrayList<BankSlipJPAEntity>();
		
		listOfBankSlipJPAEntity.add(EntitiesTransformation.convertPOJOEntityToJPAEntity(createBankSlipEntity(Optional.of(ID_TO_CREATE), LocalDate.now(), CUSTOMER,111000L, StatusEnum.PENDING.toString())));
		listOfBankSlipJPAEntity.add(EntitiesTransformation.convertPOJOEntityToJPAEntity(createBankSlipEntity(Optional.of(ID_TO_CREATE_2), LocalDate.now(), CUSTOMER,111000L, StatusEnum.PENDING.toString())));
		
		return listOfBankSlipJPAEntity;
	}

}
