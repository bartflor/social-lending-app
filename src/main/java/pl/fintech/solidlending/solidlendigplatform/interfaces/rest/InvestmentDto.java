package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import lombok.Builder;
import lombok.Value;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.Investment;

import java.time.Period;
@Builder
@Value
public class InvestmentDto {
	Long investmentId;
	Long loanId;
	String lenderName;
	double value;
	double rate;
	int duration;
	String status;
	
	public static InvestmentDto from(Investment investment) {
    return InvestmentDto.builder()
        .investmentId(investment.getInvestmentId())
        .loanId(investment.getLoanId())
        .lenderName(investment.getLenderName())
		.value(investment.getValue().getValue().doubleValue())
		.rate(investment.getRate().getPercentValue().doubleValue())
		.duration(investment.getDuration().getMonths())
		.status(investment.getStatus().toString())
        .build();
	}
}
