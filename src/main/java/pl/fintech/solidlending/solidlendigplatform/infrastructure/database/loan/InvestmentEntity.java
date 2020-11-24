package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.loan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Risk;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.Investment;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.Loan;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.RepaymentSchedule;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Period;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class InvestmentEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long investmentId;
	Long loanId;
	String lenderName;
	String borrowerName;
	BigDecimal returnAmount;
	BigDecimal loanAmount;
	BigDecimal rate;
	Integer risk;
	Period duration;
	@Enumerated(EnumType.STRING)
	Investment.Status status;
	@OneToOne(cascade = CascadeType.ALL)
	RepaymentScheduleEntity schedule;
	
	public static InvestmentEntity from(Investment investment) {
    return InvestmentEntity.builder()
        .investmentId(investment.getInvestmentId())
        .loanId(investment.getLoanId())
        .lenderName(investment.getLenderName())
        .borrowerName(investment.getBorrowerName())
        .returnAmount(investment.getReturnAmount().getValue())
        .loanAmount(investment.getLoanAmount().getValue())
        .rate(investment.getRate().getPercentValue())
        .risk(investment.getRisk().getRisk())
        .duration(investment.getDuration())
        .status(investment.getStatus())
        .schedule(RepaymentScheduleEntity.from(investment.getSchedule()))
        .build();
	}
	
	public Investment toDomain() {
		return Investment.builder()
				.investmentId(investmentId)
				.loanId(loanId)
				.lenderName(lenderName)
				.borrowerName(borrowerName)
				.returnAmount(new Money(returnAmount))
				.loanAmount(new Money(loanAmount))
				.rate(Rate.fromPercentValue(rate.doubleValue()))
				.risk(new Risk(risk))
				.duration(duration)
				.status(status)
				.schedule(schedule.toDomain())
				.build();
	}

}
