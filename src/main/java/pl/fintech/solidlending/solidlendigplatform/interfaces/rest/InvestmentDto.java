package pl.fintech.solidlending.solidlendigplatform.interfaces.rest;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Value;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.Investment;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvestmentDto {
	Long investmentId;
	Long loanId;
	String lenderName;
	String borrowerName;
	Integer risk;
	Double loanAmount;
	Double returnAmount;
	Double rate;
	Long duration;
	String status;
	List<RepaymentDto> schedule;
	
	
	public static InvestmentDto from(Investment investment) {
    return InvestmentDto.builder()
        .investmentId(investment.getInvestmentId())
        .loanId(investment.getLoanId())
        .lenderName(investment.getLenderName())
        .loanAmount(investment.getLoanAmount().getValue().doubleValue())
		.returnAmount(investment.getReturnAmount().getValue().doubleValue())
        .rate(investment.getRate().getPercentValue().doubleValue())
        .duration(investment.getDuration().toTotalMonths())
        .status(investment.getStatus().toString())
        .borrowerName(investment.getBorrowerName())
        .risk(investment.getRisk().getRisk())
        .schedule(
            investment.getSchedule().getSchedule().stream()
                .map(RepaymentDto::from)
                .sorted(Comparator.comparing(RepaymentDto::getDate))
                .collect(Collectors.toList()))
        .build();
	}
	
	public static InvestmentDto shortFrom(Investment investment) {
		return InvestmentDto.builder()
				.investmentId(investment.getInvestmentId())
				.lenderName(investment.getLenderName())
				.loanAmount(investment.getLoanAmount().getValue().doubleValue())
				.returnAmount(investment.getReturnAmount().getValue().doubleValue())
				.rate(investment.getRate().getPercentValue().doubleValue())
				.duration(investment.getDuration().toTotalMonths())
				.status(investment.getStatus().toString())
				.build();
	}
}
