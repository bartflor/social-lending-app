package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest;

import lombok.ToString;

import java.util.List;
@ToString
public class AccountDetails {
	String name;
	String accountNumber;
	double accountBalance;
	List<BankTransaction> transactions;
}
