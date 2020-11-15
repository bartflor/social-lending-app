package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest;

import lombok.*;

import java.time.Instant;
import java.time.LocalDate;
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
