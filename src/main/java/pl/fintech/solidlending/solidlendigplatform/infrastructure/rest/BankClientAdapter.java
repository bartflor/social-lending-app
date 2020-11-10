package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest;

public interface BankClientAdapter {
	
	void transfer(String sourceAccountNumber, String TargetAccountNumber, Double amount);
	
}
