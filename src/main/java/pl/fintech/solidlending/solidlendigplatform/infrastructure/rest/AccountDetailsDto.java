package pl.fintech.solidlending.solidlendigplatform.infrastructure.rest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class AccountDetailsDto {
	private String name;
	private UUID number;
	private BigDecimal accountBalance;
	private List<BankTransactionDto> transactions;

}
