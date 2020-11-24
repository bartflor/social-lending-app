package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.loan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import pl.fintech.solidlending.solidlendigplatform.domain.common.values.Money;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.Repayment;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RepaymentEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	Instant date;
	BigDecimal value;
	@Enumerated(EnumType.STRING)
	Repayment.Status status;
	
	public static RepaymentEntity from(Repayment repayment) {
		return RepaymentEntity.builder()
				.date(repayment.getDate())
				.value(repayment.getValue().getValue())
				.status(repayment.getStatus())
				.build();
	}
	
	public Repayment toDomain() {
		return Repayment.builder()
				.date(date)
				.value(new Money(value))
				.status(status)
				.build();
	}
}
