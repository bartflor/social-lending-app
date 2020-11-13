package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import lombok.Builder;
import lombok.Value;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;

import java.time.LocalDate;

@Builder
@Value
public class Repayment {
	LocalDate date;
	Money value;
	@Builder.Default Status status = Status.EXPECTED;
	
	public enum Status {
		PAID, EXPECTED, LATE
	}
}
