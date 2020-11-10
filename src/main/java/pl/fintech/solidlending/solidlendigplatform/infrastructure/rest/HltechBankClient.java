package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest;

import lombok.Data;
import lombok.extern.java.Log;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.fintech.solidlending.solidlendigplatform.infrastructure.rest.exception.TransferNotCreatedException;

@Log
@Component
@Data
public class HltechBankClient implements BankClientAdapter {
	public static final String HLTECHBANK_API_ROOT = "https://hltechbank.thebe-team.sit.fintechchallenge.pl";
	public static final String SOURCE_ACCOUNT = "sourceAccountNumber";
	public static final String TARGET_ACCOUNT = "targetAccountNumber";
	public static final String AMOUNT = "amount";
	public static final String API_TRANSACTIONS_ENDPOINT = "/transactions";
	public static final String API_ACCOUNTS_ENDPOINT = "/accounts/";
	public static final String TRANSACTION_REJECTED = "MSG";
	RestTemplate template;
	
	
	
	@Autowired
	HltechBankClient(RestTemplateBuilder builder,
					 @Value("${bank.password}") String password,
					 @Value("${bank.user}") String user,
					 RestResponseErrorHandler errorHandler){
		
		template = builder.basicAuthentication(user, password)
				.rootUri(HLTECHBANK_API_ROOT)
				.errorHandler(errorHandler)
				.build();
	}
	
	
	@Override
	public void transfer(String sourceAccountNumber, String TargetAccountNumber, Double amount) {
		JSONObject requestBody = new JSONObject();
		requestBody.put(SOURCE_ACCOUNT, sourceAccountNumber);
		requestBody.put(TARGET_ACCOUNT, TargetAccountNumber);
		requestBody.put(AMOUNT, amount);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);
		ResponseEntity<String> response = template.exchange(API_TRANSACTIONS_ENDPOINT,
				HttpMethod.POST,
				request,
				String.class);
		log.info(response.toString());
		if (!response.getStatusCode().is2xxSuccessful()) {
		  log.severe("transaction error: " + response.getBody());
		  throw new TransferNotCreatedException(TRANSACTION_REJECTED);
		}
	}
	
	public AccountDetails getDetails(String accountNumber){
		AccountDetails r =  template.getForObject(API_ACCOUNTS_ENDPOINT+accountNumber, AccountDetails.class);
		log.warning(r.toString());
		return r;
	}
	
}
