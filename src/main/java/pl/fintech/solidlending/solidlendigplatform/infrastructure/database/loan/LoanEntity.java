package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.loan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Rate;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.Investment;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.Loan;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.RepaymentSchedule;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.Period;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LoanEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	String borrowerName;
	BigDecimal amount;
	BigDecimal repayment;
	Double averageRate;
	Instant startDate;
	Period duration;
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "loanId")
	Set<InvestmentEntity> investments;
	@Enumerated(EnumType.STRING)
	Loan.LoanStatus status;
	@OneToOne(cascade = CascadeType.ALL)
	RepaymentScheduleEntity schedule;
	
	public static LoanEntity from(Loan loan){
		return LoanEntity.builder()
				.id(loan.getId())
				.borrowerName(loan.getBorrowerUserName())
				.amount(loan.getAmount().getValue())
				.repayment(loan.getRepayment().getValue())
				.averageRate(loan.getAverageRate().getPercentValue().doubleValue())
				.startDate(loan.getStartDate())
				.duration(loan.getDuration())
				.investments(loan.getInvestments().stream()
						.map(InvestmentEntity::from)
						.collect(Collectors.toSet()))
				.status(loan.getStatus())
				.schedule(RepaymentScheduleEntity.from(loan.getSchedule()))
				.build();
	}
	
	public Loan toDomain(){
		return Loan.builder()
				.id(id)
				.borrowerUserName(borrowerName)
				.amount(new Money(amount))
				.repayment(new Money(repayment))
				.averageRate(Rate.fromPercentValue(averageRate))
				.startDate(startDate)
				.duration(duration)
				.investments(investments.stream()
						.map(InvestmentEntity::toDomain)
						.collect(Collectors.toSet()))
				.schedule(schedule.toDomain())
				.status(status)
				.build();
	}
}
