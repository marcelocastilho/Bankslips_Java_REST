package com.bankslips.rest;

import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Vector;

import org.assertj.core.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
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
import com.bankslips.rest.businessrules.CancelBankSlipBusinessRulesValidation;
import com.bankslips.rest.businessrules.FineCalc;
import com.bankslips.rest.businessrules.PayBankSlipBusinessRulesValidation;
import com.bankslips.rest.enuns.RESTErrorMessages;
import com.bankslips.rest.enuns.RESTSuccessMessages;
import com.bankslips.rest.exceptions.BankSlipBusinessErrorException;
import com.bankslips.rest.exceptions.BankSlipNotFoundException;
import com.bankslips.rest.exceptions.BankSlipsValidationException;
import com.bankslips.rest.requestvalidation.RESTRequestValidation;

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
			//TODO change the request validation
			RESTRequestValidation requestValidator = new RESTRequestValidation();
			BankSlip bankSlip = requestValidator.validateCreateBankSlipRequest(request);

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

	@GetMapping
	public ResponseEntity<Object> getBankSlips() {		
		log.info("Finding all bankSlips.");
		ResponseEntity<Object> out = null;

		List<BankSlipJPAEntity> allBankSlipJPAEntity = this.bankSlipsService.getAll();

		//TODO change this transformation using reflection or another lib
		List<BankSlip> allBankSLips = new Vector<BankSlip>();
		for (BankSlipJPAEntity JPABankSLip : allBankSlipJPAEntity) {
			allBankSLips.add(EntitiesTransformation.convertJPAEntityToPOJOEntity(JPABankSLip));
		}

		out = ResponseEntity.status(HttpStatus.OK).body(allBankSLips);
		log.info("Found " +  allBankSLips.size() + " bankSlips.");
		return out;
	}

	@GetMapping("/{id}")
	public ResponseEntity<Object> getBankSlipDetails(@PathVariable(value="id") String id) {
		log.info("Finding bankSlip by id : " + id + ".");

		ResponseEntity<Object> out = null;

		try {
			//TODO change the request validation
			RESTRequestValidation requestValidator = new RESTRequestValidation();
			requestValidator.validateGetBankSlipDetailRequest(id);

			BankSlipJPAEntity bankSlipJPAEntity = this.bankSlipsService.findById(id).orElseThrow(() -> new BankSlipNotFoundException(RESTErrorMessages.CANCEL_BANKSLIP_NOT_FOUND.getMessage() + " " + id, HttpStatus.NOT_FOUND ));
			BankSlip bankSlip = EntitiesTransformation.convertJPAEntityToPOJOEntity(bankSlipJPAEntity);

			//perform the fine calculation if overdue date
			Long fine = FineCalc.calculateFine(bankSlip.getTotalInCents(), bankSlip.getDueDateDateFormat().get() );
			bankSlip.setFine(fine);
			out = ResponseEntity.status(HttpStatus.OK).body(bankSlip);

		}catch(BankSlipNotFoundException e) {
			out = ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
			log.error("Error finding a bankSlip, error: " + e.getMessage());
		} catch (BankSlipsValidationException e) {
			out = ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
			log.error("Error finding a bankSlip, error: " + e.getMessage());
		}

		log.info("Found bankSlip with id: " + id + ".");
		return out;
	}
		
	@PutMapping("/{id}/pay")
	public ResponseEntity<Object> payBankSlip(@PathVariable(value="id") String id) {
		log.info("Paying bankSlip with id " + id + ".");
		ResponseEntity<Object> out = null;

		try{
			BankSlipJPAEntity bankSLip = this.bankSlipsService.findById(id).orElseThrow(() -> new BankSlipNotFoundException(RESTErrorMessages.CANCEL_BANKSLIP_NOT_FOUND.getMessage() + " " + id, HttpStatus.NOT_FOUND ));
			
			//validating if the bankslip is in a status that could be paied
			PayBankSlipBusinessRulesValidation.validadeBankSlipPayBusinessRules(EntitiesTransformation.convertJPAEntityToPOJOEntity(bankSLip));
			
			bankSLip.setStatus(StatusEnum.PAID.toString());
			bankSlipsService.persist(bankSLip);

			out = ResponseEntity.status(HttpStatus.OK).body(RESTSuccessMessages.PAY_BANKSLIP_SUCCESS.getMessage());
			log.info("BankSLip " + id + " canceled.");

		}catch(BankSlipNotFoundException e) {
			out = ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
			log.error("Error paying a bankSlip, error: " + e.getMessage());
		}catch (BankSlipBusinessErrorException e) {
			out = ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
			log.error("Error paying a bankSlip, error: " + e.getMessage());
		}

		log.info("BankSLip " + id + " payed.");
		return out;
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Object> cancelBankSlip(@PathVariable(value="id") String id) {
		log.info("Canceling bankSlip with id " + id + ".");

		ResponseEntity<Object> out = null;

		try{
			BankSlipJPAEntity bankSLip = this.bankSlipsService.findById(id).orElseThrow(() -> new BankSlipNotFoundException(RESTErrorMessages.CANCEL_BANKSLIP_NOT_FOUND.getMessage() + " " + id, HttpStatus.NOT_FOUND ));

			//validating if the bankslip is in a status that could be canceled
			CancelBankSlipBusinessRulesValidation.validadeBankSlipCancelBusinessRules(EntitiesTransformation.convertJPAEntityToPOJOEntity(bankSLip));
			bankSLip.setStatus(StatusEnum.CANCELED.toString());
			bankSlipsService.persist(bankSLip);

			out = ResponseEntity.status(HttpStatus.OK).body(RESTSuccessMessages.CANCEL_BANKSLIP_SUCCESS.getMessage());
			log.info("BankSLip " + id + " canceled.");

		}catch(BankSlipNotFoundException e) {
			out = ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
			log.error("Error canceling a bankSlip, error: " + e.getMessage());
		}catch (BankSlipBusinessErrorException e) {
			out = ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
			log.error("Error canceling a bankSlip, error: " + e.getMessage());
		}

		return out;
	}
}