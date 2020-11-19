package pl.fintech.solidlending.solidlendigplatform.domain.payment;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.Instant;
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
