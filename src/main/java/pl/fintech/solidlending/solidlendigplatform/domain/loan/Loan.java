package pl.fintech.solidlending.solidlendigplatform.domain.loan;


import lombok.Builder;
import lombok.Data;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;

import java.time.Instant;
import java.time.Period;
import java.util.Set;
@Data
@Builder
public class Loan {
	Long id;
	String borrowerUserName;
	Money amount;
	Money repayment;
	Rate averageRate;
	Instant startDate;
	Period duration;
	Set<Investment> investments;
	@Builder.Default LoanStatus status = LoanStatus.UNCONFIRMED;
	RepaymentSchedule schedule;
	
	public enum LoanStatus {
		ACTIVE, CLOSED, UNCONFIRMED
	}
	
	public boolean isActive(){
		return status.equals(LoanStatus.ACTIVE);
	}
}
