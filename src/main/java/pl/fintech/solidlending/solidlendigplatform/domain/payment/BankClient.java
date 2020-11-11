package pl.fintech.solidlending.solidlendigplatform.domain.payment;

import pl.fintech.solidlending.solidlendigplatform.infrastructure.rest.AccountDetailsDto;

public interface BankClient {
	
	String transfer(String sourceAccountNumber, String TargetAccountNumber, Double amount);
	void createAccount(String userName);
	void payment(String accountNumber, double amount);
	AccountDetailsDto getAccountDetails(String accountNumber);
}
