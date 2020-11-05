package pl.fintech.solidlending.solidlendigplatform.domain.loan;


import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Risk;
import pl.fintech.solidlending.solidlendigplatform.domain.user.Borrower;

import java.time.Period;
import java.util.Date;
import java.util.Set;

class Loan {
	private Borrower borrower;
	private Money amount;
	private Rate averageRate;
	private Risk risk;
	private Date startDate;
	private Period Duration;
	private Set<Investment> investments;
	private LoanStatus status;
	private RepaymentSchedule schedule;

	
	public enum LoanStatus {
		ACTIVE, CLOSED
	}
}
