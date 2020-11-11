package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccountDetailsDto {
	private String name;
	private String number;
	private double accountBalance;
	private List<BankTransactionDto> transactions;

}
