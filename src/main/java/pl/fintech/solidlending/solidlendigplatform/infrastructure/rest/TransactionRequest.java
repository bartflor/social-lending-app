package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@AllArgsConstructor
@Builder
public class TransactionRequest {
	UUID sourceAccountNumber;
	UUID targetAccountNumber;
	double amount;
	
}
