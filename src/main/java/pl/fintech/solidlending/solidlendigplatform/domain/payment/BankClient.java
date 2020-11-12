package pl.fintech.solidlending.solidlendigplatform.domain.payment;

import java.math.BigDecimal;
import java.util.List;

public interface BankClient {
	
	String transfer(String sourceAccountNumber, String TargetAccountNumber, Double amount);
	void createAccount(String userName);
	void payment(String accountNumber, double amount);
	BigDecimal getAccountBalance(String accountNumber);
	List<TransactionDetails> getAccountTransactions(String accountNumber);
}
