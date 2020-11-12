package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest;

import lombok.*;

import java.time.LocalDate;
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString
@EqualsAndHashCode
@Builder
public class BankTransactionDto {
	Long id;
	String type;
	double amount;
	String referenceId;
	LocalDate timestamp;
	
}
