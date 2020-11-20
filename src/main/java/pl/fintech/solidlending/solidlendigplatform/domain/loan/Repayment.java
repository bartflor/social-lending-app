package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import lombok.Builder;
import lombok.Getter;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;

import java.time.Instant;

@Builder
@Getter
public class Repayment {
	Instant date;
	Money value;
	@Builder.Default Status status = Status.EXPECTED;
	
	public enum Status {
		PAID, EXPECTED, LATE
	}
	
	public void isPaid(){
		this.status = Status.PAID;
	}
}
