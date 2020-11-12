package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AccountDetailsDto {
	private String name;
	private String number;
	private double accountBalance;
	private List<BankTransactionDto> transactions;

}
