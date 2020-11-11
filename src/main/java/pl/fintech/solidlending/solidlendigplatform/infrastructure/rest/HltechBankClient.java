package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.fintech.solidlending.solidlendigplatform.domain.payment.BankClient;
import pl.fintech.solidlending.solidlendigplatform.infrastructure.rest.exception.TransferNotCreatedException;

import java.util.Optional;

@Component
@AllArgsConstructor
public class HltechBankClient implements BankClient {
	private HltechBankApiFeignClient apiFeignClient;
	
	@Override
	public String transfer(String sourceAccountNumber, String targetAccountNumber, Double amount) {
		ResponseEntity<String> response = apiFeignClient.transfer(TransactionRequest.builder()
												.amount(amount)
												.sourceAccountNumber(sourceAccountNumber)
												.targetAccountNumber(targetAccountNumber)
												.build());
		Optional<String> transactionResponse = Optional.ofNullable(response.getHeaders()
				.getFirst("Location"));
		return transactionResponse.map(r -> r.substring(r.lastIndexOf("/")))
				.orElseThrow(() -> new TransferNotCreatedException("MSG"));
	}
	
	@Override
	public void createAccount(String userName) {
	
	}
	
	@Override
	public void payment(String accountNumber, double amount) {
	
	}
	
	@Override
	public AccountDetailsDto getAccountDetails(String accountNumber) {
		return null;
	}
}
