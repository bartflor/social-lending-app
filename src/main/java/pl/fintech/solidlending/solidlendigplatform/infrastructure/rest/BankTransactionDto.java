package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest;

import lombok.*;

import java.time.LocalDate;

@NoArgsConstructor
@Data
@ToString
@EqualsAndHashCode
public class BankTransactionDto {
	String id;
	String type;
	double amount;
	String referenceId;
	LocalDate timestamp;
	
}
