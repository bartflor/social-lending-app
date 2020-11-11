package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "HltechBank", url = "${bank.api.url}", decode404 = true, configuration = RestCommunicationConfig.class)
public interface HltechBankApiFeignClient {
	String API_TRANSACTIONS_ENDPOINT = "/transactions";
	String API_ACCOUNTS_ENDPOINT = "/accounts/{accountNumber}";
	String API_PAYMENTS_ENDPOINT = "/payments";
	
	@RequestMapping(method = RequestMethod.POST, value = API_TRANSACTIONS_ENDPOINT,
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> transfer(TransactionRequest transactionRequest);
	
	@RequestMapping(method = RequestMethod.GET, value = API_ACCOUNTS_ENDPOINT,
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<AccountDetailsDto> accountDetails(@PathVariable("accountNumber") String accountNumber);
	
	@RequestMapping(method = RequestMethod.POST, value = API_PAYMENTS_ENDPOINT,
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> payment(PaymentRequest paymentRequest);
	
	@RequestMapping(method = RequestMethod.POST, value = API_ACCOUNTS_ENDPOINT,
			produces = MediaType.APPLICATION_JSON_VALUE)
	ResponseEntity<String> createAccount(String accountName);
	
}
