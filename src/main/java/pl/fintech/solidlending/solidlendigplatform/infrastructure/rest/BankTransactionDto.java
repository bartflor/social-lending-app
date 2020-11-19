package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class BankTransactionDto {
	Long id;
	String type;
	double amount;
	UUID referenceId;
	Instant timestamp;
	
}
