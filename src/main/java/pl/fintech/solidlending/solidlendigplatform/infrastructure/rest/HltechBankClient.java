package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest;

import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import pl.fintech.solidlending.solidlendigplatform.domain.payment.BankClient;
import pl.fintech.solidlending.solidlendigplatform.domain.payment.TransactionDetails;
import pl.fintech.solidlending.solidlendigplatform.infrastructure.rest.exception.TransferNotCreatedException;
import pl.fintech.solidlending.solidlendigplatform.infrastructure.rest.exception.BankCommunicationFailedException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
@Log
@Component
@AllArgsConstructor
public class HltechBankClient implements BankClient {
	private static final String ACCOUNT_DETAILS_NOT_PROVIDED = "Can not provide account details. Cause: %s";
	private static final String ACCOUNT_NOT_CREATED = "Account not created. Cause: %s";
	private static final String PAYMENT_FAILED = "Payment failed. Cause: %s";
	private HltechBankApiFeignClient apiFeignClient;
	
	@Override
	public String transfer(UUID sourceAccountNumber, UUID targetAccountNumber, Double amount) {
		TransactionRequest transactionRequest = TransactionRequest.builder()
				.amount(amount)
				.sourceAccountNumber(sourceAccountNumber)
				.targetAccountNumber(targetAccountNumber)
				.build();
		log.info("Transaction request: "+ transactionRequest.toString());
		ResponseEntity<String> response = apiFeignClient.transfer(transactionRequest);
		Optional<String> transactionResponse = Optional.ofNullable(response.getHeaders()
				.getFirst("Location"));
		return transactionResponse.map(r -> r.substring(r.lastIndexOf("/")+1))
				.orElseThrow(() -> new TransferNotCreatedException("MSG"));
	}
	
	@Override
	public void createAccount(String userName) {
		ResponseEntity<String> response = apiFeignClient.createAccount(userName);
		if(!response.getStatusCode().is2xxSuccessful()){
			throw new BankCommunicationFailedException(String.format(ACCOUNT_NOT_CREATED, response.toString()));
		}
	}
	
	@Override
	public void payment(UUID accountNumber, double amount) {
		ResponseEntity<String> response = apiFeignClient.payment(new PaymentRequest(accountNumber, amount));
		if(!response.getStatusCode().is2xxSuccessful()){
			throw new BankCommunicationFailedException(String.format(PAYMENT_FAILED, response.toString()));
		}
	}
	
	@Override
	public BigDecimal getAccountBalance(UUID accountNumber) {
		AccountDetailsDto detailsDto = getValidAccountDetailsResponse(accountNumber);
		return detailsDto.getAccountBalance();
	}
	
	@Override
	public List<TransactionDetails> getAccountTransactions(UUID accountNumber) {
		AccountDetailsDto detailsDto = getValidAccountDetailsResponse(accountNumber);
		return detailsDto.getTransactions().stream()
				.map(transactionDto -> TransactionDetails.builder()
						.amount(BigDecimal.valueOf(transactionDto.amount))
						.referenceId(transactionDto.referenceId)
						.timestamp(transactionDto.timestamp)
						.type(TransactionDetails.TransactionType.valueOf(transactionDto.type))
						.build())
				.collect(Collectors.toList());
	}
	
	private AccountDetailsDto getValidAccountDetailsResponse(UUID accountNumber) {
		ResponseEntity<AccountDetailsDto> dtoResponseEntity = apiFeignClient.accountDetails(accountNumber);
		if(!dtoResponseEntity.getStatusCode().is2xxSuccessful() || !dtoResponseEntity.hasBody())
			throw new BankCommunicationFailedException(String.format(ACCOUNT_DETAILS_NOT_PROVIDED, dtoResponseEntity.toString()));
		AccountDetailsDto detailsDto = dtoResponseEntity.getBody();
		return detailsDto;
	}
}
