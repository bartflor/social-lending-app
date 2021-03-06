package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import org.springframework.stereotype.Component;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.exception.ValueNotAllowedException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
class LoanFactory {
	private static final String EMPTY_INVESTMENTS_SET_NOT_ALLOWED = "Empty investment set not allowed. Can not calculate average rate from empty investment set.";
	
	/**
	 * Builds loan using provided parameters.
	 * Loan RepaymentSchedule combines each Investment RepaymentSchedule.
	 * @param params parameters of loan to create
	 * @param investments Set of investments, that together make a Loan.
	 * @return new loan with investments set and repayment schedule.
	 */
	public Loan createLoan(NewLoanParams params, Set<Investment> investments){
		Rate avgLoanRate = calculateAvgRate(investments, params.getLoanAmount());
		Money repayment = params.getLoanAmount().calculateValueWithReturnRate(avgLoanRate);
		RepaymentSchedule schedule = prepareRepaymentSchedule(investments);
		return Loan.builder()
					.borrowerUserName(params.getBorrowerUserName())
					.amount(params.getLoanAmount())
					.repayment(repayment)
					.averageRate(avgLoanRate)
					.schedule(schedule)
					.startDate(params.getLoanStartDate())
					.duration(params.getLoanDuration())
					.investments(investments)
					.build();
	}
	
	/**
	 * create RepaymentSchedule combining all investments RepaymentSchedules
	 * @param investments - set of complete investments with schedules
	 * @return one schedule for loan
	 */
	private static RepaymentSchedule prepareRepaymentSchedule(Set<Investment> investments) {
		List<RepaymentSchedule> investmentsScheduleList = investments.stream()
				.map(Investment::getSchedule)
				.collect(Collectors.toList());
		RepaymentSchedule loanSchedule = new RepaymentSchedule();
		Map<Instant, Money> totalLoanRepayToDateMap = investmentsScheduleList.stream()
				.flatMap(investmentsSchedule -> investmentsSchedule.getSchedule().stream())
				.collect(Collectors.groupingBy(Repayment::getDate,
								Collectors.reducing(
										Money.ZERO,
										Repayment::getValue,
										Money::sum)));
		loanSchedule.setSchedule(totalLoanRepayToDateMap.entrySet().stream().map(entrySet -> Repayment.builder()
						.date(entrySet.getKey())
						.value(entrySet.getValue())
						.build())
				.collect(Collectors.toList()));
		loanSchedule.setType(RepaymentSchedule.Type.LOAN);
		return loanSchedule;
	}
	
	private static Rate calculateAvgRate(Set<Investment> investments, Money loanAmount) {
		if(investments.isEmpty()){
			throw new ValueNotAllowedException(EMPTY_INVESTMENTS_SET_NOT_ALLOWED);
		}
		BigDecimal investmentsAmountSum = investments.stream()
				.map(investment -> investment.getLoanAmount().getValue()
						.multiply(investment.getRate().getPercentValue()))
				.reduce(BigDecimal::add)
				.orElse(BigDecimal.ZERO);
		BigDecimal rateValue = investmentsAmountSum.divide(loanAmount.getValue(), MathContext.DECIMAL32);
		return Rate.fromPercentValue(rateValue.doubleValue());
	}
	
}
