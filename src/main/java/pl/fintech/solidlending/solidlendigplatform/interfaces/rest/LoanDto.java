package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import lombok.Builder;
import lombok.Value;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.Investment;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.Loan;


import java.time.LocalDate;
import java.time.Period;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Value
public class LoanDto {
	Long id;
	String borrowerUserName;
	double amount;
	double repayment;
	double rate;
	LocalDate startDate;
	int duration;
	Set<InvestmentDto> investments;
	String status;
	
	
	public enum LoanStatus {
		ACTIVE, CLOSED, UNCONFIRMED
	}
	static LoanDto from(Loan loan){
    return LoanDto.builder()
        .id(loan.getId())
        .borrowerUserName(loan.getBorrowerUserName())
        .amount(loan.getAmount().getValue().doubleValue())
        .repayment(loan.getRepayment().getValue().doubleValue())
        .rate(loan.getAverageRate().getRateValue())
        .startDate(loan.getStartDate())
        .duration(loan.getDuration().getMonths())
        .investments(
            loan.getInvestments().stream().map(InvestmentDto::from).collect(Collectors.toSet()))
        .status(loan.getStatus().toString())
        .build();
	}
}
