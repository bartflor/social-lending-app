package pl.fintech.solidlending.solidlendigplatform.domain.payment;

import lombok.Builder;
import lombok.Value;
import pl.fintech.solidlending.solidlendigplatform.infrastructure.rest.BankTransactionDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Value
@Builder
public class TransactionDetails {
		TransactionType type;
		BigDecimal amount;
		UUID referenceId;
		Instant timestamp;

		public enum TransactionType{
			CREDIT, DEBIT
		}
}
