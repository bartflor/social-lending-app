package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import org.springframework.stereotype.Component;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.exception.ValueNotAllowedException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Set;

@Component
public class LoanFactory {
	private static final String EMPTY_INVESTMENTS_SET_NOT_ALLOWED = "Empty investment set not allowed. Can not calculate average rate from empty investment set.";
	
	public Loan createLoan(LoanParams params){
		Set<Investment> investments = params.getInvestments();
		Rate avgLoanRate = calculateAvgRate(investments);
		Money repayment = params.getLoanAmount().calculateValueWithReturnRate(avgLoanRate);
		RepaymentSchedule schedule = prepareRepaymentSchedule(repayment,
				params.getLoanStartDate(),
				params.getLoanDuration());
		return Loan.builder()
					.borrowerUserName(params.getBorrowerUserName())
					.amount(params.getLoanAmount())
					.repayment(repayment)
					.averageRate(avgLoanRate)
					.startDate(params.getLoanStartDate())
					.duration(params.getLoanDuration())
					.investments(investments)
					.schedule(schedule)
					.build();
	}
	
	private RepaymentSchedule prepareRepaymentSchedule(Money repayment, Instant loanStartDate, Period loanDuration) {
		RepaymentSchedule schedule = new RepaymentSchedule();
		long repaymentMonths = loanDuration.toTotalMonths() == 0 ? 1 : loanDuration.toTotalMonths();
		Money singleRepaymentAmount = repayment.divide(repaymentMonths);
		//Request repayment after 1 month;
		for(int i=1; i<=repaymentMonths; i++){
			schedule.addRepayment(loanStartDate.plus(Period.ofDays(i*30)),
					Repayment.builder().value(singleRepaymentAmount).build());
		}
		return schedule;
	}
	
	private Rate calculateAvgRate(Set<Investment> investments) {
		if(investments.isEmpty()){
			throw new ValueNotAllowedException(EMPTY_INVESTMENTS_SET_NOT_ALLOWED);
		}
		BigDecimal rateValue = investments.stream()
				.map(Investment::getRate)
				.map(Rate::getPercentValue)
				.reduce(BigDecimal::add)
				.orElse(BigDecimal.ZERO);
		rateValue = rateValue.divide(BigDecimal.valueOf(investments.size()), MathContext.DECIMAL32);
		return new Rate(rateValue);
	}
	
}
