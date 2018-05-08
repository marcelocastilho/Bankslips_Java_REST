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
public class PayBankSlipTest {

	@Autowired
	private MockMvc mvc;

	@Test
	public void GetBankSlipsDetail() throws Exception {
		mvc.perform(MockMvcRequestBuilders.put("http://localhost:8080/bankslips/84e8adbf-1a14-403b-ad73-d78ae19b59bf/pay")
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().string(equalTo("Pedido de id: 84e8adbf-1a14-403b-ad73-d78ae19b59bf Pago!")));

	}
}
