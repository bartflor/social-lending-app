package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import org.springframework.stereotype.Component;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.Set;
import java.util.stream.DoubleStream;
@Component
public class LoanFactory {
	public Loan createLoan(LoanParams params){
		Set<Investment> investments = params.getInvestments();
		Rate rate = calculateAvgRate(investments);
		Money repayment = calculateTotalRepayment(rate, params.getLoanAmount());
		RepaymentSchedule schedule = prepareRepaymentSchedule(repayment,
				params.getLoanStartDate(),
				params.getLoanDuration());
		return Loan.builder()
					.borrowerUserName(params.getBorrowerUserName())
					.amount(params.getLoanAmount())
					.repayment(repayment)
					.averageRate(rate)
					.startDate(params.getLoanStartDate())
					.duration(params.getLoanDuration())
					.investments(investments)
					.schedule(schedule)
					.build();
	}
	
	private Money calculateTotalRepayment(Rate rate, Money loanAmount) {
		BigDecimal amount = loanAmount.getValue();
		BigDecimal interest = amount.multiply(BigDecimal.valueOf(rate.getRateValue()));
		return new Money(amount.add(interest));
	}
	
	private RepaymentSchedule prepareRepaymentSchedule(Money repayment, LocalDate loanStartDate, Period loanDuration) {
		RepaymentSchedule schedule = new RepaymentSchedule();
		int repaymentMonths = loanDuration.getDays() < 30 ? 1 : loanDuration.getMonths();
		Money singleRepaymentAmount = repayment.divide(repaymentMonths);
		//Request repayment after 1 month;
		for(int i=1; i<=repaymentMonths; i++){
			schedule.addRepayment(loanStartDate.plusMonths(i),
					Repayment.builder().value(singleRepaymentAmount).build());
		}
		return new RepaymentSchedule();
	}
	
	private Rate calculateAvgRate(Set<Investment> investments) {
		double rateValue = investments.stream()
				.map(Investment::getRate)
				.flatMapToDouble(rate -> DoubleStream.of(rate.getRateValue()))
				.average()
				.getAsDouble();
		return new Rate(rateValue);
	}
	
}
