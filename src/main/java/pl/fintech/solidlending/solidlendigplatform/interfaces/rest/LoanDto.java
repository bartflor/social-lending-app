package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.Investment;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.Loan;


import java.time.Instant;
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
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "Europe/Warsaw")
	Instant startDate;
	int duration;
	Set<InvestmentDto> investments;
	String status;

	static LoanDto from(Loan loan){
    return LoanDto.builder()
        .id(loan.getId())
        .borrowerUserName(loan.getBorrowerUserName())
        .amount(loan.getAmount().getValue().doubleValue())
        .repayment(loan.getRepayment().getValue().doubleValue())
        .rate(loan.getAverageRate().getPercentValue().doubleValue())
        .startDate(loan.getStartDate())
        .duration(loan.getDuration().getMonths())
        .investments(
            loan.getInvestments().stream().map(InvestmentDto::from).collect(Collectors.toSet()))
        .status(loan.getStatus().toString())
        .build();
	}
}
