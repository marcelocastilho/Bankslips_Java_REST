package com.bankslips.rest;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
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
public class CreateBankSlipTests{

	private MockMvc mvc;

	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@MockBean
	private BankSlipRepository bankSlipRepository;

	private HttpMessageConverter<Object> httpMessageConverter;

	DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	private static final String URL_BASE = "http://localhost:8080/bankslips";
	private static final String CUSTOMER = "Teste create bankSlip";

	@Before
	public void setUp() {
		this.mvc = webAppContextSetup(webApplicationContext).build();
	}

	@Autowired
	@SuppressWarnings("unchecked")
	public void setConverters(HttpMessageConverter<?>[] converters) {
		this.httpMessageConverter = (HttpMessageConverter<Object>) Arrays.asList(converters).stream()
				.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().orElse(null);
		assertNotNull("the JSON message converter must not be null", this.httpMessageConverter);
	}

	/**
	 * This test will verify if the REST method CreateBankSlip is ok
	 */
	@Test
	@WithMockUser(username="admin")
	public void CreateBankSlipSuccess() throws Exception {
		
		BankSlipJPAEntity bankSlipJPAEntity = EntitiesTransformation.convertPOJOEntityToJPAEntity(createBankSlipEntity(Optional.of("Teste"), LocalDate.now(), CUSTOMER, 111000L, StatusEnum.PENDING.toString()));
		
		BDDMockito.given(this.bankSlipRepository.save(Mockito.any(BankSlipJPAEntity.class))).willReturn(bankSlipJPAEntity); 
			
		//Creating a correct bankSlip object
		String bankSlipJson = convertInJason(createBankSlipEntity(null, LocalDate.now(), CUSTOMER, 111000L, StatusEnum.PENDING.toString()));

		//calling and validating the rest method response
		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(bankSlipJson))
		.andExpect(status().isOk());
		
	}

	/**
	 * This test will verify if the request contains a valid BankSlip
	 */
	@Test
	@WithMockUser(username="admin")
	public void CreateBankSlipErrorRequest() throws Exception {

		//Creating a problematic bankSlip object
		String bankSlip = "Problematic bankSlip";

		//calling and validating the rest method response
		mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.content(bankSlip))
		.andExpect(status().isBadRequest())
		.andExpect(content().string(RESTErrorMessages.CREATE_BANKSLIP_INVALID_REQUEST.getMessage()));	
	}

	/**
	 * This test will verify if the request contains a valid date in the object BankSlip
	 */
	@Test
	@WithMockUser(username="admin")
	public void CreateBankSlipErrorInvalidDate() throws Exception {
		
		//Creating a correct bankSlip object
		String bankSlipJson = convertInJason(createBankSlipEntity(null, null, CUSTOMER, 111000L, null));

		//calling and validating the rest method response
				mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.content(bankSlipJson))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(content().string(RESTErrorMessages.CREATE_BANKSLIP_INVALID_BANKSLIP_DATA.getMessage()));
	}
	
	private BankSlip createBankSlipEntity(Optional<String> id, LocalDate dueDate, String customer, long totalInCents, String status) {
		BankSlip BankSlip = new BankSlip();
		BankSlip.setId(id);
		if(dueDate != null)
			BankSlip.setDueDate(dateFormatter.format(dueDate));
		BankSlip.setCustomer(customer);
		BankSlip.setTotalInCents(totalInCents);
		BankSlip.setStatus(status);
		return BankSlip;
	}
	
	protected String convertInJason(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.httpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}
}
