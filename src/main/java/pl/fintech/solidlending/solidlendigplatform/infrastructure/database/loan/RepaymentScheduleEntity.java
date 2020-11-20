package pl.fintech.solidlending.solidlendigplatform.infrastructure.database.loan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.Repayment;
import pl.fintech.solidlending.solidlendigplatform.domain.loan.RepaymentSchedule;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RepaymentScheduleEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	Long ownerId;
	@OneToOne
	LoanEntity loanEntity;
	@OneToOne
	InvestmentEntity investmentEntity;
	@OneToMany(cascade = CascadeType.ALL)
	List<RepaymentEntity> repayments;
	@Enumerated(EnumType.STRING)
	RepaymentSchedule.Type type;
	
	public static RepaymentScheduleEntity from(RepaymentSchedule schedule) {
		return RepaymentScheduleEntity.builder()
				.id(schedule.getId())
				.ownerId(schedule.getOwnerId())
				.repayments(schedule.getSchedule().stream()
						.map(RepaymentEntity::from)
						.collect(Collectors.toList()))
				.type(schedule.getType())
				.build();
	}
	
	public RepaymentSchedule toDomain() {
		ownerId = loanEntity == null ? 0 : loanEntity.getId();
		ownerId = (ownerId == 0 && investmentEntity != null) ? investmentEntity.getInvestmentId() : 0;
		return RepaymentSchedule.builder()
				.id(id)
				.ownerId(ownerId)
				.schedule(repayments.stream()
						.map(RepaymentEntity::toDomain)
						.collect(Collectors.toList()))
				.build();
	}
}
