package com.bankslips.rest;

import static org.junit.Assert.assertNotNull;

import java.util.Calendar;
import java.util.Date;

import org.assertj.core.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bankslips.entities.BankSlip;
import com.bankslips.entities.StatusEnum;
import com.bankslips.entities.datatransformation.EntitiesTransformation;
import com.bankslips.jpa.entities.BankSlipJPAEntity;
import com.bankslips.jpa.services.BankSlipsService;
import com.bankslips.rest.enuns.RESTErrorMessages;
import com.bankslips.rest.enuns.RESTSuccessMessages;
import com.bankslips.rest.exceptions.BankSlipsValidationException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/bankslips")
@CrossOrigin(origins = "*")
public class BankSlipsRestManager {

	private static final Logger log = LoggerFactory.getLogger(BankSlipsRestManager.class);
	
	private HttpMessageConverter<Object> httpMessageConverter;

	@Autowired
	private BankSlipsService bankSlipsService;
	
	@SuppressWarnings("unchecked")
	public void setConverters(HttpMessageConverter<?>[] converters) {
		this.httpMessageConverter = (HttpMessageConverter<Object>) Arrays.asList(converters).stream()
				.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().orElse(null);
		assertNotNull("the JSON message converter must not be null", this.httpMessageConverter);
	}

	@PostMapping
	public ResponseEntity<Object> createBankSlip(@RequestBody String request) {
		log.info("Creating new bankSlip.");
		ResponseEntity<Object> out = null;

		try {

			BankSlip bankSlip = validateCreateBankSlipRequest(request);

			bankSlip.setStatus(StatusEnum.PENDING.toString());
			BankSlipJPAEntity bankSlipSaved = bankSlipsService.persist(EntitiesTransformation.convertEntityToJPAEntity(bankSlip));

			out = ResponseEntity.status(HttpStatus.OK).body(RESTSuccessMessages.CREATE_BANKSLIP_SUCCESS);
			log.info("New bankSlip with id: " + bankSlipSaved.getId());
		} catch (BankSlipsValidationException e) {
			out = ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
			log.info("Error creating a new bankSlip, error: " + e.getMessage());
		}

		
		return out; 

		/*		201 : Bankslip created
		● 400 : Bankslip not provided in the request body
		● 422 : Invalid bankslip provided.The possible reasons are:
		○ A field of the provided bankslip was null or with invalid values
		 */
	}

	@GetMapping("/{id}")
	public BankSlip getBankSlipDetails(@PathVariable(value="id") String id) {
		log.info("Finding bankSlip by id : " + id + ".");


		log.info("Found bankSlip with id: " + id + ".");
		return new BankSlip(new Date(Calendar.getInstance().getTimeInMillis()), 100000, "Marcelo Castilho");
	}

	@GetMapping
	public BankSlip getBankSlips() {		
		log.info("Finding all bankSlips.");
		//List<BankSlipJPAEntity> bankSlips = null;
		//this.empresaService.getAll().stream().map(EmpresaResource::new)				.collect(Collectors.toList());
		long count = 90;
		log.info("Found " +  count + " bankSlips.");
		return new BankSlip(new Date(Calendar.getInstance().getTimeInMillis()), 100000, "Marcelo Castilho");
	}

	@PutMapping("/{id}/pay")
	public String payBankSlip(@PathVariable(value="id") String id) {
		log.info("Paying bankSlip with id " + id + ".");


		log.info("BankSLip " + id + " payed.");
		return "Pedido de id: " + id + " Pago!";
	}

	@DeleteMapping("/{id}")
	public String cancelBankSlip(@PathVariable(value="id") String id) {
		log.info("Canceling bankSlip with id " + id + ".");

		log.info("BankSLip " + id + " Canceled.");
		return "Pedido de id: " + id + " Cancelado!";
	}

	/**
	 * This method verify the request content of rest operation createBankSlip
	 * @param request
	 * @return BankSlip
	 * @throws BankSlipsValidationException 
	 */
	private BankSlip validateCreateBankSlipRequest(String request) throws BankSlipsValidationException {

		BankSlip bankSlip;
		try {
			bankSlip = new ObjectMapper().readValue(request, BankSlip.class);
		} catch (Exception e) {
			//I considered that any error in this parser must response the same message 
			throw new BankSlipsValidationException(RESTErrorMessages.CREATE_BANKSLIP_INVALID_REQUEST.getMessage(), HttpStatus.BAD_REQUEST);
		}

		if(StringUtils.isEmpty(bankSlip.getCustomer()) 
				|| bankSlip.getDueDate() == null
				|| bankSlip.getTotalInCents() == 0){
			throw new BankSlipsValidationException(RESTErrorMessages.CREATE_BANKSLIP_INVALID_BANKSLIP_DATA.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return bankSlip;
	}
}