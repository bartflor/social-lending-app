package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class TransactionRequest {
	String sourceAccountNumber;
	String targetAccountNumber;
	double amount;
	
}
