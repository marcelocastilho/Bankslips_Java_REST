package com.bankslips.rest;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
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

import com.bankslips.main.SprintBootStarter;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=SprintBootStarter.class)
@AutoConfigureMockMvc
public class GetBankSlipDetailsTest {

	@Autowired
	private MockMvc mvc;

	@Test
	public void GetBankSlipDetails() throws Exception {
		String bankSlipId = "84e8adbf-1a14-403b-ad73-d78ae19b59bf";	
		mvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/bankslips")
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(bankSlipId)));

	}
}
