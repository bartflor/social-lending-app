package pl.fintech.solidlending.solidlendigplatform.domain.payment;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface BankClient {
	
	String transfer(String sourceAccountNumber, String TargetAccountNumber, Double amount);
	void createAccount(String userName);
	void payment(String accountNumber, double amount);
	BigDecimal getAccountBalance(UUID accountNumber);
	List<TransactionDetails> getAccountTransactions(UUID accountNumber);
}
