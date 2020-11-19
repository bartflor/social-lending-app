package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.Loan;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
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
	long duration;
	Set<InvestmentDto> investments;
	List<RepaymentDto> schedule;
	String status;

	static LoanDto from(Loan loan){
		return LoanDto.builder()
			.id(loan.getId())
			.borrowerUserName(loan.getBorrowerUserName())
			.amount(loan.getAmount().getValue().doubleValue())
			.repayment(loan.getRepayment().getValue().doubleValue())
			.rate(loan.getAverageRate().getPercentValue().doubleValue())
			.startDate(loan.getStartDate())
			.duration(loan.getDuration().toTotalMonths())
			.investments(loan.getInvestments().stream()
					.map(InvestmentDto::shortFrom)
					.collect(Collectors.toSet()))
			.schedule(loan.getSchedule().getSchedule().stream()
					.map(RepaymentDto::from)
					.sorted(Comparator.comparing(RepaymentDto::getDate))
					.collect(Collectors.toList()))
			.status(loan.getStatus().toString())
			.build();
	}
}
