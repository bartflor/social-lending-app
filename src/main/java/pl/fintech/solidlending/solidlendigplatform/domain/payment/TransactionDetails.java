package pl.fintech.solidlending.solidlendigplatform.domain.payment;

import lombok.Builder;
import lombok.Value;
import pl.fintech.solidlending.solidlendigplatform.infrastructure.rest.BankTransactionDto;

import java.time.LocalDate;
import java.util.List;
@Value
@Builder
public class TransactionDetails {
		String type;
		double amount;
		String referenceId;
		LocalDate timestamp;

}
