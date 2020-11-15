package pl.fintech.solidlending.solidlendigplatform.domain.loan;

import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;

import java.time.Instant;
import java.time.Period;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InvestmentFactory {
	public static Set<Investment> createInvestmentsFrom(List<NewInvestmentParams> newInvestmentsParamsList){
		Set<Investment> investments = new HashSet<>();
		for(NewInvestmentParams params: newInvestmentsParamsList){
		Money value = params.getInvestedMoney().calculateValueWithReturnRate(params.getReturnRate());
		Period investmentDuration = params.getInvestmentDuration();
		Instant startDate = params.getInvestmentStartDate();
		investments.add(Investment.builder()
				.lenderName(params.getLenderUserName())
				.loanAmount(params.getInvestedMoney())
				.value(value)
				.rate(params.getReturnRate())
				.duration(investmentDuration)
				.schedule(prepareRepaymentSchedule(investmentDuration, value, startDate))
				.build());
		}
		return investments;
	}
	
	public static RepaymentSchedule prepareRepaymentSchedule(Period investmentDuration, Money value, Instant startDay) {
		RepaymentSchedule schedule = new RepaymentSchedule();
		long repaymentMonths = investmentDuration.toTotalMonths() == 0 ? 1 : investmentDuration.toTotalMonths();
		Money singleRepaymentAmount = value.divide(repaymentMonths);
		//Request repayment after 1 month;
		for(int i=1; i<=repaymentMonths; i++){
			schedule.addRepayment(startDay.plus(Period.ofDays(i*30)),
					singleRepaymentAmount);
		}
		schedule.setType(RepaymentSchedule.Type.INVESTMENT);
		return schedule;
	}
}
