package com.bankslips.rest;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.bankslips.entities.BankSlip;
import com.bankslips.main.SprintBootStarter;
import com.bankslips.rest.enuns.RESTErrorMessages;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SprintBootStarter.class)
@AutoConfigureMockMvc
public class CreateBankSlipTests{

	@Autowired
	private MockMvc mvc;

	@Autowired
	private WebApplicationContext webApplicationContext;

	private HttpMessageConverter<Object> httpMessageConverter;

	private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
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
	public void CreateBankSlipSuccess() throws Exception {

		//Creating a correct bankSlip object
		String bankSlipJson = convertInJason(createBankSlipEntity(null, Calendar.getInstance().getTime(), CUSTOMER, 111000L));

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
	public void CreateBankSlipErrorRquest() throws Exception {

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
	public void GetBankSlipDetails() throws Exception {
		
		//Creating a correct bankSlip object
		String bankSlipJson = convertInJason(createBankSlipEntity(null, null, CUSTOMER, 111000L));

		//calling and validating the rest method response
				mvc.perform(MockMvcRequestBuilders.post(URL_BASE)
					.contentType(MediaType.APPLICATION_JSON)
					.accept(MediaType.APPLICATION_JSON)
					.content(bankSlipJson))
				.andExpect(status().isUnprocessableEntity())
				.andExpect(content().string(RESTErrorMessages.CREATE_BANKSLIP_INVALID_BANKSLIP_DATA.getMessage()));
	}
	
	private BankSlip createBankSlipEntity(Optional<String> id, Date dueDate, String customer, long totalInCents) {
		BankSlip BankSlip = new BankSlip();
		BankSlip.setId(id);
		BankSlip.setDueDate(dateFormat.format(dueDate));
		BankSlip.setCustomer(customer);
		BankSlip.setTotalInCents(totalInCents);
		return BankSlip;
	}

	protected String convertInJason(Object o) throws IOException {
		MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
		this.httpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
		return mockHttpOutputMessage.getBodyAsString();
	}
}
