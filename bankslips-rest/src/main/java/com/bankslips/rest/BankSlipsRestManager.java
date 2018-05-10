package com.bankslips.rest;

import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

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
import com.bankslips.rest.exceptions.BankSlipNotFoundException;
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
	
	@PostMapping
	public ResponseEntity<Object> createBankSlip(@Valid @RequestBody String request) {
		log.info("Creating new bankSlip.");
		ResponseEntity<Object> out = null;

		try {
			//TODO Make better the request validation
			BankSlip bankSlip = validateCreateBankSlipRequest(request);

			bankSlip.setStatus(StatusEnum.PENDING.toString());
			BankSlipJPAEntity bankSlipJPA = bankSlipsService.persist(EntitiesTransformation.convertPOJOEntityToJPAEntity(bankSlip));

			out = ResponseEntity.status(HttpStatus.OK).body(RESTSuccessMessages.CREATE_BANKSLIP_SUCCESS.getMessage());
			
			log.info("New bankSlip with id: " + bankSlipJPA.getId());
			
		} catch (BankSlipsValidationException  e) {
			out = ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
			log.error("Error creating a new bankSlip, error: " + e.getMessage());
		}

		return out; 
	}

	@GetMapping("/{id}")
	public List<BankSlipJPAEntity> getBankSlipDetails(@PathVariable(value="id") String id) {
		log.info("Finding bankSlip by id : " + id + ".");
		List<BankSlipJPAEntity> allBankSLips = this.bankSlipsService.getAll();

		log.info("Found bankSlip with id: " + id + ".");
		return allBankSLips;
	}

	@GetMapping
	public ResponseEntity<Object> getBankSlips() {		
		log.info("Finding all bankSlips.");
		ResponseEntity<Object> out = null;
		
		List<BankSlipJPAEntity> bankSlips = this.bankSlipsService.getAll().stream().collect(Collectors.toList());
		out = ResponseEntity.status(HttpStatus.OK).body(bankSlips);
		
		log.info("Found " +  bankSlips.size() + " bankSlips.");
		return out;
	}

	@PutMapping("/{id}/pay")
	public String payBankSlip(@PathVariable(value="id") String id) {
		log.info("Paying bankSlip with id " + id + ".");


		log.info("BankSLip " + id + " payed.");
		return "Pedido de id: " + id + " Pago!";
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> cancelBankSlip(@PathVariable(value="id") String id) {
		log.info("Canceling bankSlip with id " + id + ".");
		
		ResponseEntity<Object> out = null;
		
		try{
			BankSlipJPAEntity bankSLip = this.bankSlipsService.findById(id).orElseThrow(() -> new BankSlipNotFoundException(RESTErrorMessages.CANCEL_BANKSLIP_NOT_FOUND.getMessage() + " " + id, HttpStatus.NOT_FOUND ));
		
			bankSLip.setStatus(StatusEnum.CANCELED.toString());
			bankSlipsService.persist(bankSLip);
			
			out = ResponseEntity.status(HttpStatus.OK).body(RESTSuccessMessages.CANCEL_BANKSLIP_SUCCESS.getMessage());
			log.info("BankSLip " + id + " canceled.");
			
		}catch(BankSlipNotFoundException e) {
			out = ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
			log.error("Error canceling a bankSlip, error: " + e.getMessage());
		}
		
		return out;
	}
	
	
	@SuppressWarnings("unchecked")
	public void setConverters(HttpMessageConverter<?>[] converters) {
		this.httpMessageConverter = (HttpMessageConverter<Object>) Arrays.asList(converters).stream()
				.filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter).findAny().orElse(null);
		assertNotNull("the JSON message converter must not be null", this.httpMessageConverter);
	}

	
	/**
	 * 
	 * @return Resources<EmpresaResource>
	 */
	/*@GetMapping
	public Resources<EmpresaResource> readEmpresas() {
		log.info("Finding paied bankslips ");
		List<BankSlip> paiedBankSLip = this.bankSlipsService.getAll().stream().map(BankSlip::new)
				.collect(Collectors.toList());
		return new Resources<>(paiedBankSLip);
	}*/

	/**
	 * This method verify the request content of rest operation createBankSlip
	 * @param request
	 * @return BankSlip
	 * @throws BankSlipsValidationException 
	 */
	private BankSlip validateCreateBankSlipRequest(String request) throws BankSlipsValidationException {

		BankSlip bankSlip = null;
		
		try {
			// Try to parse the request object to java bankSlip object
			bankSlip = new ObjectMapper().readValue(request, BankSlip.class);
		} catch (Exception e) {
			// Any error in this parser must response the same message Invalid Request
			throw new BankSlipsValidationException(RESTErrorMessages.CREATE_BANKSLIP_INVALID_REQUEST.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
		//validating the fields 
		if(StringUtils.isEmpty(bankSlip.getCustomer()) 
				|| bankSlip.getDueDate() == null
				|| bankSlip.getTotalInCents() == 0){
			throw new BankSlipsValidationException(RESTErrorMessages.CREATE_BANKSLIP_INVALID_BANKSLIP_DATA.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		}
		return bankSlip;
	}
}